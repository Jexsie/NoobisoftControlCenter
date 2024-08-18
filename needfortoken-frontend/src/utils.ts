export function setActiveUser(email: string) {
  return localStorage.setItem("user", email);
}

export function getUser() {
  return localStorage.getItem("user");
}
