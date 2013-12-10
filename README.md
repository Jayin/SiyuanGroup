SiyuanGroup
===========

SiyuanGroup is committed to create a new social space for the alumni .

Members:
---
* Fritz Lium 
* Jayin Ton
* 张剑伟
* huangruichang


Code Rules
---
 com.sina.weibo.sdk.codestyle.CodingRuler


@SuppressWarnings(value={"unused"})

类的大体描述放在这里。 

NOTE：以下部分为一个简要的编码规范，更多规范请参考 ORACLE 官方文档。
地址：http://www.oracle.com/technetwork/java/codeconventions-150003.pdf
另外，请使用 UTF-8 格式来查看代码，避免出现中文乱码。
至于注释应该使用中文还是英文，请自己行决定，根据公司或项目的要求而定，推荐使用英文。


1. 整理代码
 1.1. Java 代码中不允许出现在警告，无法消除的警告要用 @SuppressWarnings。 
 1.2. 去掉无用的包、方法、变量等，减少僵尸代码。 
 1.3. 使用 Lint 工具来查看并消除警告和错误。 
 1.4. 使用 Ctrl+Shift+F 来格式化代码，然后再进行调整。 
 1.5. 使用 Ctrl+Shift+O 来格式化 Import 包。 
2. 命名规则
2.1. 基本原则
2.1.1. 变量，方法，类命名要表义，严格禁止使用 name1, name2 等命名。 
2.1.2. 命名不能太长，适当使用简写或缩写。（最好不要超过 25 个字母） 
2.1.3. 方法名以小写字母开始，以后每个单词首字母大写。 
2.1.4. 避免使用相似或者仅在大小写上有区别的名字。 
2.1.5. 避免使用数字，但可用 2 代替 to，用 4 代替 for 等，如 go2Clean。 
2.2. 类、接口
2.2.1. 所有单词首字母都大写。使用能确切反应该类、接口含义、功能等的词。一般采用名词。 
2.2.2. 接口带 I 前缀，或able, ible, er等后缀。如ISeriable。 
2.3. 字段、常量
2.3.1. 成员变量以 m 开头，静态变量以 s 开头，如 mUserName, sInstance。 
2.3.2. 常量全部大写，在词与词之前用下划线连接，如 MAX_NUMBER。 
2.3.3. 代码中禁止使用硬编码，把一些数字或字符串定义成常用量。 
2.3.4. 对于废弃不用的函数，为了保持兼容性，通常添加 @Deprecated，如 doSomething() 
3. 注释
请参考 SampleCode 类的注释。 
3.1. 常量注释，参见 ACTION_MAIN 
3.2. 变量注释，参见 mObject0 
3.3. 函数注释，参见 doSomething(int, float, String) 
4. Class 内部顺序和逻辑
4.1. 每个 class 都应该按照一定的逻辑结构来排列基成员变量、方法、内部类等， 从而达到良好的可读性。 
4.2. 总体上来说，要按照先 public, 后 protected, 最后 private, 函数的排布 也应该有一个逻辑的先后顺序，由重到轻。 
4.3. 以下顺序可供参考： 定义TAG，一般为 private（可选）
定义 public 常量
定义 private 常量、内部类
定义 private 变量
定义 public 方法
定义 protected 方法
定义 private 方法

5. 表达式与语句
5.1. 基本原则：采用紧凑型风格来编写代码
5.2. 细则
5.2.1. 条件表示式，参见 conditionFun(boolean) 
5.2.2. switch 语句，参见 switchFun(int) 
5.2.3. 循环语句，参见 circulationFun(boolean) 
5.2.4. 错误与异常，参见 exceptionFun() 
5.2.5. 杂项，参见 otherFun() 
5.2.6. 批注，参见 doSomething(int, float, String) 
Since:
2013-XX-XX
Author:
作者名
