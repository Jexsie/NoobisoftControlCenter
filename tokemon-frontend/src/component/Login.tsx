import { useCallback, useState } from "react";
import { useNavigate } from "react-router-dom";
import { login } from "../api";
import { setActiveUser } from "../utils";
export default function Login() {
  const navigate = useNavigate();
  const [user, setUser] = useState({ email: "", accountId: "" });

 const handleLogin = useCallback(async (e) => {
  e.preventDefault()
  if(!user.email || !user.accountId) return

  await login(user).then((res) => {
    return res.status === 200 ? (setActiveUser(user.email), navigate('/dashboard')) : null
  }).catch((err) => console.error(err.message))
 }, [])
  return (
    <div
      className="w-full h-screen flex justify-center items-center"
      style={{ backdropFilter: "blur(25px)" }}
    >
      <div
        style={{ height: "60vh" }}
        className="w-full max-w-sm p-4 bg-white border border-gray-200 rounded-lg shadow sm:p-6 md:p-8 dark:bg-gray-800 dark:border-gray-700"
      >
        <form className="space-y-6" onSubmit={handleLogin}>
          <h5 className="text-xl font-medium text-gray-900 dark:text-white">
            Sign in to our platform
          </h5>
          <div>
            <label
              id="email"
              className="block mb-2 text-sm font-medium text-gray-900 dark:text-white"
            >
              Your email
            </label>
            <input
              type="email"
              name="email"
              id="email"
              className="bg-gray-50 border border-gray-300 text-gray-900 text-sm rounded-lg focus:ring-blue-500 focus:border-blue-500 block w-full p-2.5 dark:bg-gray-600 dark:border-gray-500 dark:placeholder-gray-400 dark:text-white"
              placeholder="name@company.com"
              onChange={(e) =>
                setUser((prev) => ({ ...prev, email: e.target.value }))
              }
              value={user.email}
              required
            />
          </div>
          <div>
            <label
              id="accountId"
              className="block mb-2 text-sm font-medium text-gray-900 dark:text-white"
            >
              Your account ID
            </label>
            <input
              type="password"
              name="accountId"
              id="accountId"
              placeholder="••••••••"
              className="bg-gray-50 border border-gray-300 text-gray-900 text-sm rounded-lg focus:ring-blue-500 focus:border-blue-500 block w-full p-2.5 dark:bg-gray-600 dark:border-gray-500 dark:placeholder-gray-400 dark:text-white"
              onChange={(e) =>
                setUser((prev) => ({ ...prev, accountId: e.target.value }))
              }
              value={user.accountId}
              required
            />
          </div>
          <button
            onClick={handleLogin}
            className="w-full text-white bg-blue-700 hover:bg-blue-800 focus:ring-4 focus:outline-none focus:ring-blue-300 font-medium rounded-lg text-sm px-5 py-2.5 text-center dark:bg-blue-600 dark:hover:bg-blue-700 dark:focus:ring-blue-800"
          >
            Login / Create account
          </button>
        </form>
      </div>
    </div>
  );
}
