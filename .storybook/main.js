const config = {
    stories: ["../components/**/*.stories.@(js|jsx)"],
    addons: [
        "@storybook/addon-docs",
        "@storybook/addon-a11y"
    ],
    framework: {
        name: "@storybook/react-vite",
        options: {}
    }
};

export default config;
