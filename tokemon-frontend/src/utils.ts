export function setActiveUser(email: string) {
  return sessionStorage.setItem("user", email);
}

export function getUser() {
  return sessionStorage.getItem("user");
}
