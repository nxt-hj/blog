```es6
//给元素赋 鼠标焦点缩放 拖拽事件
function scaleAndDrag(svgDom){
    let scale = 1;
    let enter = false;
    let spaceKey = false;
    let move: MouseEvent;
    let dragStart: MouseEvent;
    let wheelStart: string[];
    let svgStart: string[];
    const width = parseInt(svgDom.getAttribute('width'), 10);
    const height = parseInt(svgDom.getAttribute('height'));
    function wheel(event: WheelEvent) {
        if (!event.ctrlKey || !enter) {
            return;
        }
        event.preventDefault();
        event.stopPropagation();
        const target = event.currentTarget as HTMLElement;
        let nScale = scale;
        if (event.deltaY > 0 && scale > 1) {
            nScale = scale - 0.1;
        }
        if (event.deltaY < 0 && scale < 3) {
            nScale = scale + 0.1;
        }
        if (nScale === scale) {
            return;
        }
        const translateXRatio = -(move.offsetX / width) * (nScale - scale);
        const translateYRatio = -(move.offsetY / height) * (nScale - scale);
        const tranlateX = translateXRatio * width;
        const tranlateY = translateYRatio * height;
        const transform = `translate(${tranlateX + +wheelStart[0]}px,${tranlateY + +wheelStart[1]}px) scale(${nScale})`;
        target.style.transform = transform;
        scale = nScale;
        wheelStart = transform.match(/-{0,1}\d+\.{0,1}\d+/g);
    }

    //按键
    function keydown(event: KeyboardEvent) {
        spaceKey = event.key === ' ';
        if (spaceKey) {
            svgDom.style.cursor = 'move';
        }
    }
    function keyup() {
        spaceKey = false;
        svgDom.style.cursor = 'default';
    }
    function mouseenter() {
        enter = true;
    }
    function mouseleave() {
        enter = false;
    }
    function mousemove(event: MouseEvent) {
        move = event;
        wheelStart = svgDom.style.transform.match(/-{0,1}\d+\.{0,1}\d+/g) || ['0', '0'];
        if (dragStart && spaceKey && svgStart) {
            svgDom.style.transform = `translate(${+svgStart[0] + event.clientX - dragStart.clientX}px,${
                +svgStart[1] + event.clientY - dragStart.clientY
            }px) scale(${scale})`;
        }
    }
    function mousedown(event: MouseEvent) {
        dragStart = event;
        svgStart = svgDom.style.transform.match(/-{0,1}\d+\.{0,1}\d+/g);
    }
    function mouseup() {
        dragStart = null;
        svgStart = null;
    }
    svgDom.addEventListener('wheel', wheel, { passive: false });
    svgDom.addEventListener('click', click);
    svgDom.addEventListener('mouseenter', mouseenter);
    svgDom.addEventListener('mouseleave', mouseleave);
    svgDom.addEventListener('mousemove', mousemove);
    svgDom.addEventListener('mousedown', mousedown);
    svgDom.addEventListener('mouseup', mouseup);
    document.body.addEventListener('keydown', keydown);
    document.body.addEventListener('keyup', keyup);
}
```
