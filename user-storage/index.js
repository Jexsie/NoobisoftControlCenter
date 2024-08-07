// user-storage/index.js
const USER_STORAGE_KEY = "user_storage";

// Helper function to get stored users
function getStoredUsers() {
  const users = localStorage.getItem(USER_STORAGE_KEY);
  return users ? JSON.parse(users) : [];
}

// Helper function to save users to localStorage
function saveUsers(users) {
  localStorage.setItem(USER_STORAGE_KEY, JSON.stringify(users));
}

// Function to add a user
export function addUser(user) {
  const users = getStoredUsers();
  users.push(user);
  saveUsers(users);
}

// Function to list all user emails
export function listUserEmails() {
  const users = getStoredUsers();
  return users.map((user) => user.email);
}

export function checkUser(email) {
  const users = getStoredUsers();
  const isAvailable = users.some((user) => user.email === email);
  const userData = isAvailable
    ? users.filter((user) => user.email === email)
    : null;

  return { isAvailable, userData };
}

export function updateUserStatus(user) {
  const users = getStoredUsers();
  const updatedUsers = users.map((u) =>
    u.email === user.email ? { ...u, loggedIn: user.loggedIn } : u
  );
  saveUsers(updatedUsers);
}

export function useActiveUser(app) {
  const users = getStoredUsers();
}
