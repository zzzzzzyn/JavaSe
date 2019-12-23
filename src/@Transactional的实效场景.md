## @Transactional不生效情况：
1. 抛出异常不为RuntimeException,@Transactional默认只能对RuntimeException进行回滚
2. @Transactional注解修饰的方法不是public的
3. 在同一个类中，没有@Transactional注解的方法去调用有@Transactional注解

## 解决：
1. @Transactional(rollbackFor = Exception.class)可对Exception进行回滚
2. @Transactional注解修饰的方法修改为public方法
3. 调用方法也加上@Transactional

## 个人想法：
可以把异常trycatch住，然后包装成RuntimeException抛出，这样就可以直接使用默认的@Transactional
```java
// 业务代码
@Transactional
public void service() throws RuntimeException{
    try {
        userMapper.updateA(1, "1111");
        userMapper.updateB(5, "5555");
        throw new Exception("异常");
    } catch (Exception e) {
        // slf4j可以把放入的Exception堆栈信息打印出来，不用我们去手动调用堆栈
        logger.error("错误信息{}",e);
        // 此处包装成RuntimeException或继承RuntimeException的子类继续抛出
        throw new RuntimeException(e);
    }
}
```

     