/** @type {import('tailwindcss').Config} */
import tailwindForms from "@tailwindcss/forms";
export default {
  content: ["./index.html", "./src/**/*.{js,ts,jsx,tsx}"],
  theme: {
    fontFamily: {
      nunito: ["nunito", "sans-serif"],
      bebas: ['"Bebas Neue"', "sans-serif"],
    },
    extend: {},
  },
  plugins: [tailwindForms],
};
