### Test中使用的框架
MVI

### Question 2
Test中使用了MVI框架，MVI框架可简单概括为：单向流，响应式，传输数据不可变，接受用户意图，响应界面状态。下面是MVI主要流程图。
![MVI流程图](https://raw.githubusercontent.com/oldergod/android-architecture/todo-mvi-rxjava-kotlin/art/MVI_detail.png)

所以在测试的时候我们只需要用户输入的意图`MviIntent`输出的界面状态`MviViewState`是否与预期相等即可。在测试用我们使用`Rxjava2`的`TestObserver#assertValueAt`来测试响应位置的界面状态是否正确。如果你愿意的话可以查看我写的博客了解更多关于MVI的思想[https://littlegnal.github.io/2018-01-22/state-manage-mvi](https://littlegnal.github.io/2018-01-22/state-manage-mvi)。

由于本人对于UI测试不熟悉，所以Test中没有进行UI测试。