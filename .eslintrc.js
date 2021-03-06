module.exports = {
  extends: 'airbnb',
  parser: 'babel-eslint',
  env: {
    browser: true,
    jest: true,
  },
  rules: {
    'arrow-parens': ['error', 'as-needed'],
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
    'react/state-in-constructor': 'off',
    'react/static-property-placement': ['warn', 'static public field'],
    'object-curly-spacing': [1],
    'object-curly-newline': 'off',
  },
};
