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
        'react/destructuring-assignment': 'off',
        'react/no-did-update-set-state': 'off',
        'react/jsx-one-expression-per-line': 'off',
        'import/no-named-as-default': 'off',
        'import/prefer-default-export': 'off',
        'object-curly-spacing': [1],
    },
};
