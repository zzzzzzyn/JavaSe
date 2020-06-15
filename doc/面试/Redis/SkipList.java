package com.xyn.mall;

import java.util.Random;

/**
 * 跳表
 * https://blog.csdn.net/DERRANTCM/article/details/79063312
 * Created by xyn on 2020/6/15
 */
public class SkipList {

    // 头结点
    public SkipListEntry head;
    // 尾结点
    public SkipListEntry tail;
    // entry个数
    public Integer n;
    // SkipList高
    public Integer h;
    // random
    public Random r;

    // 初始化
    public SkipList() {

        SkipListEntry p1, p2;
        p1 = new SkipListEntry(SkipListEntry.NEG_INF, Integer.MIN_VALUE);
        p2 = new SkipListEntry(SkipListEntry.POS_INF, Integer.MAX_VALUE);

        // p1,p2关联
        p1.right = p2;
        p2.left = p1;

        // head,tail初始化
        head = p1;
        tail = p2;

        n = 0;
        h = 0;
        r = new Random();
    }

    // 内部类
    class SkipListEntry {

        public static final String NEG_INF = "-oo";
        public static final String POS_INF = "+oo";

        // data
        public String key;
        public Integer value;

        // link
        public SkipListEntry up;
        public SkipListEntry down;
        public SkipListEntry left;
        public SkipListEntry right;

        public SkipListEntry(String key, Integer value) {
            this.key = key;
            this.value = value;
        }
    }

    // 1.从head出发，因为head指向最顶层（top level）链表的开始节点，相当于从顶层开始查找；
    // 2.移动到当前节点的右指针（right）指向的节点，直到右节点的key值大于要查找的key值时停止；
    // 3.如果还有更低层次的链表，则移动到当前节点的下一层节点（down），如果已经处于最底层，则退出；
    // 4.重复第2步 和 第3步，直到查找到key值所在的节点，或者不存在而退出查找；
    private SkipListEntry findEntry(String key) {

        SkipListEntry p = head;

        while (true) {

            // 移动到当前节点的右指针（right）指向的节点，直到右节点的key值大于要查找的key值时停止；
            while (p.right.key != SkipListEntry.POS_INF && p.right.key.compareTo(key) <= 0) {
                p = p.right;
            }

            // 如果还有更低层次的链表，则移动到当前节点的下一层节点（down），如果已经处于最底层，则退出；
            if (p.down != null) {
                p = p.down;
            } else {
                break;
            }

        }

        return p;
    }

    public Integer get(String key) {
        SkipListEntry p = findEntry(key);

        if (p.key.equals(key)) {
            return p.value;
        } else {
            return null;
        }
    }

    // 覆盖或新增
    public Integer put(String key, Integer value) {

        int i = 0;

        // 覆盖
        SkipListEntry entry = findEntry(key);
        if (entry.key.equals(key)) {
            Integer oldValue = entry.value;
            entry.value = value;
            return oldValue;
        }

        // 新增
        // 建立关联
        SkipListEntry newEntry = new SkipListEntry(key, value);
        newEntry.left = entry;
        newEntry.right = entry.right;
        entry.right.left = newEntry;
        entry.right = newEntry;

        // 再使用随机数决定是否要向更高level攀升
        while (r.nextDouble() < 0.5) {
            // 如果新元素的级别已经达到跳跃表的最大高度，则新建空白层
            if (i >= h) {
                addEmptyLevel();
            }

            // 从entry向左扫描含有高层节点的节点
            while (entry.up == null) {
                entry = entry.left;
            }
            entry = entry.up;

            // 新增和q指针指向的节点含有相同key值的节点对象
            // 这里需要注意的是除底层节点之外的节点对象是不需要value值的
            SkipListEntry upNewEntry = new SkipListEntry(key, null);
            // 跟下层建立关联
            upNewEntry.down = newEntry;
            newEntry.up = upNewEntry;

            upNewEntry.left = entry;
            upNewEntry.right = entry.right;
            entry.right.left = upNewEntry;
            entry.right = upNewEntry;

            newEntry = upNewEntry;
            i++;
        }

        n++;

        return null;
    }

    // 添加空白层
    private void addEmptyLevel() {
        SkipListEntry p1, p2;
        p1 = new SkipListEntry(SkipListEntry.NEG_INF, Integer.MIN_VALUE);
        p2 = new SkipListEntry(SkipListEntry.POS_INF, Integer.MAX_VALUE);

        p1.right = p2;
        p2.left = p1;

        head.up = p1;
        tail.up = p2;

        p1.down = head;
        p2.down = tail;

        head = p1;
        tail = p2;

        h++;
    }

    // 移除
    public Integer remove(String key) {
        SkipListEntry entry;
        entry = findEntry(key);

        if (!entry.key.equals(key)) {
            return null;
        }

        Integer oldValue = entry.value;

        // 移除所有
        while (entry != null) {
            entry.left.right = entry.right;
            entry.right.left = entry.left;
            entry = entry.up;
        }

        return oldValue;
    }

    public static void main(String[] args) {
        SkipList skipList = new SkipList();
        skipList.put("1", 22);
        skipList.put("2", 33);
        skipList.put("8", 88);
        skipList.remove("8");
        System.out.println(skipList.get("8"));
    }
}
