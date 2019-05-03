module.exports = {
    extends: 'airbnb',
    parser: 'babel-eslint',
    env: {
        jest: true,
    },
    rules: {
        'max-len': [1, 120],
        indent: ['error', 2, { SwitchCase: 1 }],
        'react/jsx-indent': [2, 2],
        'react/jsx-indent-props': [2, 2],
        'linebreak-style': 'off',
        'react/destructuring-assignment': [0],
        'react/no-did-update-set-state': [0],
        'react/jsx-one-expression-per-line': [0],
        'import/no-named-as-default': [0],
        'object-curly-spacing': [1],
    },
};
