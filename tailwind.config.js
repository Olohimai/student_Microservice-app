module.exports = {
  mode: process.env.NODE_ENV ? 'jit' :undefined,
  purge:["./src/**/*.html","./src/**/*.js"]
  content: [],
  theme: {
    extend: {},
  },
  plugins: [],
}
