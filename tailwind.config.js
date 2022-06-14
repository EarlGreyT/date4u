module.exports = {
  mode: process.env.NODE_ENV ? 'jit' : undefined,
  content: ["./src/**/*.html"],
  darkMode: 'media', // or 'media' or 'class'
  theme: {

    extend: {
        fontSize:{
            'xxs':'.70rem'
        }
    },
  },
   plugins: [
    require('@tailwindcss/forms')
	],
}