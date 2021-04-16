当多人或者多团队协作开发时，为了方便维护和管理，需要代码风格保持一致，不过只靠个人自觉很难实现，这时候就需要借助工具了,这里推荐eslint+prettier+husky+pretty-quick实现在开发和提交时自动进行检测
# 介绍
## [eslint](https://github.com/eslint/eslint/)
一款代码语法和规则检测工具，能自定义检测规则，且能定义问题级别（0-关闭，1-告警，2-报错），如
```es6
"rules": {
    "constructor-super": 2,//在constuctor中必须调用super
    "no-unused-vars": 2,//不允许定义没有使用的变量
    ...
 }
```
如果代码不满足条件，则抛出错误，那么现在我们通过eslint-loader把eslint集成到webpack中

**安装**
`npm i -D eslint-loader`
```es6
{
rules:[{
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
    include: path.resolve(__dirname, "src"),
    exclude: [path.resolve(__dirname, "src/platform/UDS/static"), path.resolve(__dirname, "src/platform/MRO/static")]
  }]
}
```
