const API = 'http://localhost:8080';
let token = '';
let userId = 0;
let editingJobId = null;

// ── PAGE NAVIGATION ──────────────────────────────
function showPage(pageId) {
    document.querySelectorAll('.page').forEach(p => p.classList.add('hidden'));
    document.getElementById(pageId).classList.remove('hidden');
}

// ── REGISTER ─────────────────────────────────────
async function register() {
    const username = document.getElementById('regUsername').value;
    const email = document.getElementById('regEmail').value;
    const password = document.getElementById('regPassword').value;

    if (!username || !email || !password) {
        document.getElementById('registerError').textContent = 'All fields are required!';
        return;
    }

    const response = await fetch(`${API}/api/auth/register`, {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({ username, email, password })
    });

    const text = await response.text();

    if (response.ok) {
        document.getElementById('registerSuccess').textContent = 'Account created! Please login.';
        document.getElementById('registerError').textContent = '';
        setTimeout(() => showPage('loginPage'), 1500);
    } else {
        document.getElementById('registerError').textContent = text;
        document.getElementById('registerSuccess').textContent = '';
    }
}

// ── LOGIN ─────────────────────────────────────────
async function login() {
    const username = document.getElementById('loginUsername').value;
    const password = document.getElementById('loginPassword').value;

    if (!username || !password) {
        document.getElementById('loginError').textContent = 'Please enter username and password!';
        return;
    }

    const response = await fetch(`${API}/api/auth/login`, {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({ username, password })
    });

    if (response.ok) {
        const data = await response.json();
        token = data.token;
        userId = data.userId;
        document.getElementById('welcomeUser').textContent = 'Welcome, ' + data.username + '!';
        showPage('dashboardPage');
        loadJobs();
    } else {
        document.getElementById('loginError').textContent = 'Wrong username or password!';
    }
}

// ── LOGOUT ───────────────────────────────────────
function logout() {
    token = '';
    userId = 0;
    showPage('loginPage');
}

// ── LOAD JOBS ────────────────────────────────────
async function loadJobs() {
    const response = await fetch(`${API}/api/jobs/user/${userId}`, {
        headers: { 'Authorization': 'Bearer ' + token }
    });

    const jobs = await response.json();
    renderJobs(jobs);
    updateStats(jobs);
}

// ── RENDER JOBS ──────────────────────────────────
function renderJobs(jobs) {
    const list = document.getElementById('jobsList');

    if (jobs.length === 0) {
        list.innerHTML = `
            <div class="empty-state">
                <p>No job applications yet!</p>
                <p>Click "+ Add Job" to get started.</p>
            </div>`;
        return;
    }

    list.innerHTML = jobs.map(job => `
        <div class="job-card">
            <div class="job-info">
                <h4>${job.company} — ${job.role}</h4>
                <p>${job.appliedDate ? 'Applied: ' + job.appliedDate : ''} 
                   ${job.notes ? '• ' + job.notes : ''}</p>
                <span class="status-badge status-${job.status}">${job.status}</span>
            </div>
            <div class="job-actions">
                <button class="btn btn-edit" onclick="editJob(${job.id})">Edit</button>
                <button class="btn btn-delete" onclick="deleteJob(${job.id})">Delete</button>
            </div>
        </div>
    `).join('');
}

// ── UPDATE STATS ─────────────────────────────────
function updateStats(jobs) {
    document.getElementById('totalCount').textContent = jobs.length;
    document.getElementById('interviewCount').textContent =
        jobs.filter(j => j.status === 'Interview').length;
    document.getElementById('offerCount').textContent =
        jobs.filter(j => j.status === 'Offer').length;
    document.getElementById('rejectedCount').textContent =
        jobs.filter(j => j.status === 'Rejected').length;
}

// ── SHOW/HIDE FORM ────────────────────────────────
function showAddForm() {
    editingJobId = null;
    document.getElementById('formTitle').textContent = 'Add Job Application';
    document.getElementById('saveBtn').textContent = 'Save';
    document.getElementById('company').value = '';
    document.getElementById('role').value = '';
    document.getElementById('status').value = 'Applied';
    document.getElementById('appliedDate').value = '';
    document.getElementById('notes').value = '';
    document.getElementById('jobForm').classList.remove('hidden');
}

function hideForm() {
    document.getElementById('jobForm').classList.add('hidden');
}

// ── SAVE JOB (ADD or EDIT) ────────────────────────
async function saveJob() {
    const company = document.getElementById('company').value;
    const role = document.getElementById('role').value;

    if (!company || !role) {
        alert('Company and Role are required!');
        return;
    }

    const jobData = {
        company,
        role,
        status: document.getElementById('status').value,
        appliedDate: document.getElementById('appliedDate').value || null,
        notes: document.getElementById('notes').value,
        userId
    };

    const url = editingJobId
        ? `${API}/api/jobs/${editingJobId}`
        : `${API}/api/jobs`;

    const method = editingJobId ? 'PUT' : 'POST';

    await fetch(url, {
        method,
        headers: {
            'Content-Type': 'application/json',
            'Authorization': 'Bearer ' + token
        },
        body: JSON.stringify(jobData)
    });

    hideForm();
    loadJobs();
}

// ── EDIT JOB ──────────────────────────────────────
async function editJob(id) {
    const response = await fetch(`${API}/api/jobs/${id}`, {
        headers: { 'Authorization': 'Bearer ' + token }
    });

    const job = await response.json();
    editingJobId = id;

    document.getElementById('formTitle').textContent = 'Edit Job Application';
    document.getElementById('saveBtn').textContent = 'Update';
    document.getElementById('company').value = job.company;
    document.getElementById('role').value = job.role;
    document.getElementById('status').value = job.status;
    document.getElementById('appliedDate').value = job.appliedDate || '';
    document.getElementById('notes').value = job.notes || '';
    document.getElementById('jobForm').classList.remove('hidden');
}

// ── DELETE JOB ────────────────────────────────────
async function deleteJob(id) {
    if (!confirm('Are you sure you want to delete this job?')) return;

    await fetch(`${API}/api/jobs/${id}`, {
        method: 'DELETE',
        headers: { 'Authorization': 'Bearer ' + token }
    });

    loadJobs();
}

// Allow pressing Enter to login
document.addEventListener('keypress', function(e) {
    if (e.key === 'Enter') {
        const loginPage = document.getElementById('loginPage');
        if (!loginPage.classList.contains('hidden')) login();
    }
});
