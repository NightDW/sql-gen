// 提交表单
function submit() {
    document.getElementById("form").submit();
}

// 当用户按下回车后，提交表单
function submitIfEnter(event) {
    if (event.keyCode == 13) {
        submit();
    }
}

// 添加一个join组件
function addJoin(button) {

    // 获取父节点以及join组件个数
    var joins = document.getElementById('joins');
    var joinCount = joins.getElementsByClassName('join').length;

    // 根据join组件模板来创建join组件
    var joinTemplate = document.getElementById('joinTemplate');
    var join = document.createElement('p');
    join.innerHTML = joinTemplate.innerHTML;

    // 完善join组件
    join.setAttribute('class', 'join');
    join.removeAttribute('id');
    join.removeAttribute('hidden');
    var selects = join.getElementsByTagName("select");
    selects[0].setAttribute('name', 'joins[' + joinCount + '].type');
    selects[1].setAttribute('name', 'joins[' + joinCount + '].tableName');

    // 添加join组件
    joins.removeChild(button.parentNode);
    joins.appendChild(join);
    joins.appendChild(button.parentNode);
}

// 删除join组件，并提交（主要是为了刷新页面）
function deleteJoin(button) {
    button.parentNode.parentNode.removeChild(button.parentNode);
    submit();
}

// 多选框的全选/全不选功能
function checkAll(button) {

    // 获取到此次操作涉及到的所有input组件
    var list = button.parentNode.getElementsByTagName('input');

    // 判断这些input组件是否都被选中；如果是，则更新成全不选，否则更新成全选
    var isAllChecked = true;
    for (var i = 0; i < list.length; i++) {
        if (list[i].getAttribute('type') === 'checkbox' && !list[i].checked) {
            isAllChecked = false;
            break;
        }
    }

    // 更新状态
    for (var i = 0; i < list.length; i++) {
        if (list[i].getAttribute('type') === 'checkbox') {
            list[i].checked = !isAllChecked;
        }
    }
}

// 生成结果
function generate() {
    var form = document.getElementById("form");
    var oldAction = form.getAttribute('action');
    var newAction = oldAction.replace('submit', 'generate');
    form.setAttribute('action', newAction);
    form.submit();
    form.setAttribute('action', oldAction);
}