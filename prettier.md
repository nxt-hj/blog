当多人或者多团队协作开发时，为了方便维护和管理，需要代码风格保持一致，不过只靠个人自觉很难实现，这时候就需要借助工具了,这里推荐eslint+prettier+husky+pretty-quick实现在开发和提交时自动进行检测

## [eslint](https://github.com/eslint/eslint/)
代码语法和规则检测工具，能自定义检测规则，且能定义问题级别（0-关闭，1-告警，2-报错，也有通过on和off设置的），如
```es6
"rules": {
    "constructor-super": 2,//在constuctor中必须调用super
    "no-unused-vars": 2,//变量未使用
    "no-undef": 2,//变量未定义
    ...
 }
```
如果代码不满足条件，则抛出错误，如果这样一个个去定义，想必非常耗费时间，这时你可以使用extends扩展规则包，eslint自带一个扩展"eslint:recommended",其他则需要通过npm去安装了，通过在rules设置，可以覆盖规则包里的同名规则
```es6
"extends": [
    "react-app",
    "plugin:jsx-a11y/recommended",
    "eslint:recommended",
    "plugin:react/recommended"
],
```
如果使用了webpack的ProvidePlugin全局引入，直接使用全局变量eslint检测到会报错，可以设置globals
```es6
"globals": {
    "globalVariableName":true
}
```
那么现在我们通过eslint-loader把eslint集成到webpack中

**安装**
`npm i -D eslint-loader`
```es6
{
    rules: [
        // { enforce: "pre", test: /\.js$/, loader: "source-map-loader" },
        {
            test: /\.(js|jsx|ts)$/,
            enforce: 'pre',
            use: [
                {
                    options: {
                        formatter: eslintFormatter,
                        eslintPath: require.resolve('eslint'),
                        // baseConfig: {
                        //     extends: [require.resolve('eslint-config-react-app')],
                        // },
                        ignore: false,
                        useEslintrc: true,
                    },
                    loader: require.resolve('eslint-loader'),
                },
            ],
            include: path.resolve(__dirname, 'src'),
            exclude: [
                path.resolve(__dirname, 'src/platform/UDS/static'),
                path.resolve(__dirname, 'src/platform/MRO/static'),
            ],
        },
    ]
}
```
## [prettier](https://prettier.io/docs/en/index.html)
格式化美化代码的插件，提升阅读性，代码维护起来更方便，个人使用的配置如下
```js
module.exports = {
    trailingComma: 'es5',
    tabWidth: 4,
    semi: false,
    singleQuote: true,
    jsxSingleQuote: true,
    bracketSpacing: true,
    jsxBracketSameLine: false,
    arrowParens:'always'
}
```
