package com.heima.takeout.utils

// 定义观察者接口
interface Observer {
    fun onUpdate(value: Int)
}

// 定义被观察的对象
class Subject {
    // 使用委托属性来管理观察者列表
    private val observers = mutableListOf<Observer>()

    // 添加观察者
    fun addObserver(observer: Observer) {
        observers.add(observer)
    }

    // 删除观察者
    fun removeObserver(observer: Observer) {
        observers.remove(observer)
    }

    // 当状态更新时通知观察者
    fun notifyObservers(value: Int) {
        observers.forEach { it.onUpdate(value) }
    }
}

// 创建观察者类
class ConcreteObserver(private val name: String) : Observer {
    override fun onUpdate(value: Int) {
        println("$name 收到更新，新值为 $value")
    }
}

fun main() {
    // 创建被观察对象
    val subject = Subject()

    // 创建观察者并添加到被观察对象中
    val observer1 = ConcreteObserver("观察者1")
    val observer2 = ConcreteObserver("观察者2")
    subject.addObserver(observer1)
    subject.addObserver(observer2)

    // 模拟状态更新并通知观察者
    subject.notifyObservers(10)

    // 观察者2取消观察
    subject.removeObserver(observer2)

    // 再次更新状态并通知观察者
    subject.notifyObservers(20)
}
