#### 常见问题

1. 事务四大特性？
   - 原子性
   - 持久性
   - 一致性：执行事务前后，数据保持一致，多个事务对统一数据读取结果相同
   - 隔离性：事务未提交前，修改数据互相不可见
   
2. 事务隔离级别及可能产生的问题？
   - 读未提交
   
   - 读已提交
   
   - 可重复读
   
   - 串行化
   
     | 隔离级别 | 脏读 | 不可重复读 | 幻读 |
     | :------: | :--: | :--------: | :--: |
     | 读未提交 |  √   |     √      |  √   |
     | 读已提交 |  ×   |     √      |  √   |
     | 可重复读 |  ×   |     ×      |  √   |
     |  序列化  |  ×   |     ×      |  ×   |
   
3. InnoDB和MyISAM？

   - InnoDB采用MVCC支持高并发，实现事务四个隔离级别，默认是可重复读，通过**间隙锁策略**来防止幻读，因为间隙锁的原因，所以是行锁
   - MyISAM，不支持事务，表锁，崩溃后无法安全恢复

4. **索引的优点？**

   - 大大减少服务器需要扫描的数据量
   - 帮助服务器避免排序和临时表
   - 将随机I/O变为顺序I/O

5. **B-TREE索引生效场景**？

   - 假设创建了一个索引 people(last_name, first_name, birthday)，索引生效场景有
   - **全值匹配**，例如`where last_name =  xxx and first_name = xxx and birthday = xxx`
   - **最左前缀(这个经常问)**，例如`where last_name = xxx and birthday = xxx`，这就只用到了last_name这一列，而像`where first_name = xxx and birthday = xxx`就不会用到索引
   - **匹配列前缀**，例如`where last_name like 'J%'`，这个也会用到索引，不过只使用了last_name这一列
   - **匹配范围值**，例如：没有例如，还没试

6. **B-TREE索引限制？**

   - 必须从最左列开始查找，否则无法使用，例如：`where first_name = xxx and birthday = xxx`就无法使用索引
   - 不能跳过索引中的列，例如：`where last_name = xxx and birthday = xxx`就只使用了last_name这列，birthday这一列没有生效
   - 如果查询中某列为范围查询，则范围查询右边的列都是失效，例如：`where last_name = xxx and first_name list '%s' and birthday = xxx`，此时birthday列的索引失效，只使用到了前两列
   - 列必须是独立的，否则无法使用，例如：`where last_name + 'x' = xxxx`，这就不会用到索引

7. **什么是覆盖索引**？

   - 访问的字段都在索引中，叫做覆盖索引，效率贼鸡儿高

8. 高性能索引？

   - 独立的列
   - 前缀索引和索引选择性
   - 多列索引（建立索引名：column1_column2_column3，这样好知道都包含哪些列及顺序）
   - 选择合适的索引顺序

9. 使用索引排序？

   - 索引列顺序和ORDER BY子句顺序**完全一致**，且所有列排序方向（ASC，DESC）都一样时，才会使用索引进行排序，也是**最左前缀原则**
   - **有一种情况可以不满足最左前缀要求**，**就是前导列为常量的时候**，例如索引列（rental_date，inventory_id，customer_id），SQL为`select * from rental where rental_data = '2005-5-5' ordery by inventory_id,customer_id`也可生效

10. 不能使用索引排序情况？

    - 查询使用两种**不同排序方向**，例如：`where rental_data = '2020-1-1 order by inventory_id desc, customer_id asc'`
    - ORDER BY引用了不在索引中的列，例如：`order by rental_data ,staff_id`
    - WHERE和ORDER BY中的列无法组成最左前缀，例如：`where rental_data = '2020-1-1' order by customer_id`
    - 索引第一列是范围条件，例如：`where rental_data>'2020-1-1' order by inventory_id, customer_id`