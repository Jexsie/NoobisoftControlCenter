export function setActiveUser(accountId: string) {
  return sessionStorage.setItem("user", accountId);
}

export function getUser() {
  return sessionStorage.getItem("user");
}
