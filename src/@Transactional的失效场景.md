Spring对事务异常的处理
- unchecked 运行时异常-->会进行事务回滚，如：RuntimeException
- checked 异常-->不会进行事务回滚，如：Exception

### @Transactional不生效情况：
1. 抛出异常不为RuntimeException,@Transactional默认只能对RuntimeException进行回滚
2. @Transactional注解修饰的方法不是public的
3. 在同一个类中，没有@Transactional注解的方法去调用有@Transactional注解

### 解决：
1. @Transactional(rollbackFor = Exception.class)可对Exception进行回滚
2. @Transactional注解修饰的方法修改为public方法
3. 调用方法也加上@Transactional

### 个人想法：
可以把异常trycatch住，然后包装成RuntimeException抛出，这样就可以直接使用默认的@Transactional
```java
// 业务代码
@Transactional
public void service() {
    try {
        userMapper.updateA(1, "1111");
        userMapper.updateB(5, "5555");
        throw new Exception("异常");
    } catch (Exception e) {
        // slf4j可以把放入的Exception堆栈信息打印出来，不用我们去手动调用堆栈
        logger.error("这里是错误信息!",e);
        // 此处包装成RuntimeException或继承RuntimeException的自定义异常继续抛出
        throw new RuntimeException(e);
    }
}
```

### 事务传播行为

| Propagation                    |                                                          |
| ------------------------------ | -------------------------------------------------------- |
| **PROPAGATION_REQUIRED(默认)** | 支持当前事务，如果当前没有事务，就新建一个事务           |
| **PROPAGATION_SUPPORTS**       | 支持当前事务，如果当前没有事务，就以非事务方式执行       |
| **PROPAGATION_MANDATORY**      | 支持当前事务，如果当前没有事务，就抛出异常               |
| **PROPAGATION_REQUIRES_NEW**   | 新建事务，如果当前存在事务，把当前事务挂起               |
| **PROPAGATION_NOT_SUPPORTED**  | 以非事务方式执行操作，如果当前存在事务，就把当前事务挂起 |
| **PROPAGATION_NEVER**          | 以非事务方式执行，如果当前存在事务，则抛出异常           |

