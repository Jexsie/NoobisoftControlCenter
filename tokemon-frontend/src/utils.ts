export function setActiveUser(accountId: string) {
  return sessionStorage.setItem("user", accountId);
}
export function getUser() {
  return sessionStorage.getItem("user");
}

export function removeUser() {
  return sessionStorage.removeItem("user");
}
