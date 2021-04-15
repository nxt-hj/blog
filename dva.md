# 介绍
在开发过程中，每次都需要编写装载和卸载生命周期去卸载样式style和模型model，为了减少这些重复工作，需要包裹一层高阶组件加入自定义逻辑

# _connect
注意：基于react，且要使用style-loader和dva(redux+redux-saga)，不使用dva的，可以将model相关代码删除即可，只保留style的自动装载和卸载
```es6
/**connect包裹 */
export function _connect() {

    if (Object.prototype !== arguments[0].__proto__) {
        return connect(...arguments)
    }

    const argumentsArray = [...arguments];
    const [{ model, style }, mapStateToProps] = argumentsArray;
    const styles = style instanceof Array || !style ? style : [style];
    const models = model instanceof Array || !model ? model : [model];
    models && models.forEach(model => { _app.model(model) });

    return function (Component) {
        const WrappedComponent = function (props) {
            React.useLayoutEffect(function () {
                styles && styles.forEach(style => { style && style.use() });
                return function () {
                    styles && styles.forEach(style => { style && style.unuse() });
                    models && models.forEach(model => { _app.unmodel(model.namespace) });
                }
            }, [])
            return <Component {...props} />
        }
        return connect(function (props) {
            if (!models) return mapStateToProps(props)
            const hasModel = _app._models.findIndex(existModel => existModel.namespace === models[0].namespace);
            if (hasModel === -1) {
                let nProps = { ...props };
                models.forEach(model => {
                    nProps = { ...nProps, [model.namespace]: model.state }
                    _app.model(model)
                });
                // try {
                //     mapStateToProps(props);
                // } catch (err) {

                // }
                return mapStateToProps(nProps)
            }
            return mapStateToProps(props)
        }, ...argumentsArray.slice(2, argumentsArray.length))(WrappedComponent)
    }

}
```
# 引用
- 函数式hooks
```es6
import model from 'xxx.model.js';
import style from 'xxx.scss';
const MCCSituation = ({ modules, dispatch,refresh }) => {
    const fontSize = useFontsize();
    React.useLayoutEffect(() => {
        dispatch({ type: 'MCCSituation/init', modules });
        let refreshInterval = setInterval(() => {
            dispatch({ type: 'MCCSituation/init', modules });
        }, 5 * 60 * 1000)
        return () => {
            clearInterval(refreshInterval)
        }
    }, [data.itemId])
    return (
        <div className="GridLayout-box MCCSituation" style={{ fontSize }}>
            {React.useMemo(() =>
                <div className="GridLayout-back">
                    return (
                        <GridLayout _exportName="Grid" title={moduleName} >
                            <Module modules={modules}/>
                        </GridLayout>
                    )
                </div>
                , [refresh]
            )}
        </div>
    )
}

export default _connect({ model, style }, ({ MCCSituation: { modules,refresh } }) => ({ modules, refresh }))(MCCSituation)
```
- 类class 装饰器
```es6
import model from 'xxx.model.js';
import style from 'xxx.scss';

@_connect({model,style},({ menu }) => ({ ...menu }))
class Example extends React.Component{
    render() {
        const { collapsed, dispatch } = this.props;
        return (
            <Icon
                className="menu-trigger"
                type={collapsed ? 'menu-unfold' : 'menu-fold'}
                onClick={() => {
                    dispatch({ type: "menu/setCollapsed", collapsed: !collapsed })
                }}
            />
        )
    }
}
```
- 当然你也可以不传model或者style，当做redux的connect来使用
```es6
@_connect(mapStateToProps?: Function,
  mapDispatchToProps?: Function,
  mergeProps?: Function,
  options?: Object)
```
