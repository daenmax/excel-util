## ExcelUtil使用说明

### 介绍

该工具类基于阿里巴巴easyexcel二次封装开发，用于快速实现 导入、导出（单sheet、多sheet）的excel文件 ，同时提供了字典翻译注解、数据脱敏注解

字典翻译注解支持导入导出时自动转换字典值，支持简易字典明细和自定义字典明细两种方式

数据脱敏注解支持导出时自动脱敏

### 快速使用

##### 1.在项目pom中引入

```xml
<dependency>
    <groupId>cn.daenx.lib</groupId>
    <artifactId>excel-util</artifactId>
    <version>1.0.0</version>
</dependency>
```

##### 2.在实体类上添加注解

```java
@ExcelProperty(value = "字段名称")
```

##### 3.导出

单sheet表测试

```java
@PostMapping("/exportData")
public void exportData(HttpServletResponse response){
        List<StudentVo> studentList=...;
        ExcelUtil.exportXlsx(response,"单sheet表测试","学生信息",studentList,StudentVo.class);
        }
```

多sheet表测试

```java
@PostMapping("/exportData")
public void exportData(HttpServletResponse response){
        ExcelWriter writer=ExcelUtil.createExport(response,"多sheet表测试");

        List<ClassVo> classList=...;
        ExcelUtil.writeSheet(writer,"班级信息",classList,ClassVo.class);

        List<StudentVo> studentList=...;
        ExcelUtil.writeSheet(writer,"学生信息",studentList,StudentVo.class);

        ExcelUtil.finishWrite(writer);
        }
```

##### 4.导入

单sheet表测试

```java
@PostMapping("/importData")
public void importData(@RequestPart("file") MultipartFile file)throws IOException{
        ExcelResult<StudentVo> studentResult=ExcelUtil.importExcel(file.getInputStream(),StudentVo.class);
        List<StudentVo> studentList=studentResult.getList();
        System.out.println(studentList);
        }
```

多sheet表测试

```java
@PostMapping("/importData")
public void importData(@RequestPart("file") MultipartFile file)throws IOException{
        ExcelReader reader=ExcelUtil.createImport(file.getInputStream());
        ReadRetVo<ClassVo> sheetA=ExcelUtil.readSheet("班级信息",ClassVo.class);
        ReadRetVo<StudentVo> sheetB=ExcelUtil.readSheet("学生信息",StudentVo.class);
        ExcelUtil.finishRead(reader,sheetA,sheetB);

        ExcelResult<ClassVo> classResult=ExcelUtil.transResult(sheetA);
        ExcelResult<StudentVo> studentResult=ExcelUtil.transResult(sheetB);

        List<ClassVo> classList=classResult.getList();
        List<StudentVo> studentList=studentResult.getList();
        System.out.println(classList);
        System.out.println(studentList);
        }
```

### 高级功能

目前有`字典翻译`和`数据脱敏`

如果你要使用这两个功能的任意一个，那么需要在你项目中继承`ExcelConverterCore`（例如本demo中的`ExcelConverter`，文章下文均以此举例）

然后在`@ExcelPropert`注解中的`converter`参数指定为`ExcelConverter.class`

#### 功能一：数据脱敏

在导出时，对需要进行脱敏的字段使用@Masked注解

MaskedType是内置的枚举类型，内置支持的数据类型有：姓名，手机号，身份证号码，银行卡号，电子邮箱，地址信息，IP地址

```java
@ExcelProperty(value = "学生姓名", converter = ExcelConverter.class)
@Masked(type = MaskedType.NAME)
private String studentName;
```

如果你希望使用自己的脱敏算法，那么你可以在`ExcelConverter`中重写`mask`方法：

```
/**
 * 数据脱敏
 * 如果你不想使用内置的脱敏算法，那么可以通过重写该方法来实现使用自己的脱敏算法
 *
 * @param type  数据类型，@Masked的type参数
 * @param value 待脱敏的数据值
 * @return
 */
@Override
public String masked(Integer type, String value)
```

然后你你就可以在`@Masked`注解中传入你需要的type，并且在重写的`mask`方法中接收到，然后处理并返回你的脱敏结果

#### 功能二：字典翻译

在导入导出时，对需要进行字典翻译的字段使用`@Dict`注解

该注解有两个参数

| 参数                  | 释义     |
|---------------------|--------|
| String dictCode     | 字典编码   |
| DictDetail[] custom | 简易字典明细 |

这两个参数二选一即可

##### 使用简易字典明细

如果你选择使用简易字典明细，案例如下，该种方式是将字典明细写死在代码中

```java
@ExcelProperty(value = "学生性别", converter = ExcelConverter.class)
@Dict(custom = {@DictDetail(value = "0", label = "女"), @DictDetail(value = "1", label = "男")})
private String studentSex;
```

##### 使用字典编码

如果你选择使用字典编码，案例如下，该种方式可以通过其他数据源进行翻译，例如redis、数据库等

```java
@ExcelProperty(value = "学生性别", converter = ExcelConverter.class)
@Dict(dictCode = "student_sex", custom = {})
private String studentSex;
```

同时需要在`ExcelConverter`中重写`dictToLabel`、`dictToValue`方法，分别用来将字典值转换到标签、字典标签转换到值

```java
/**
 * 字典转换：字典值转换到标签
 * 你可在该方法中查询你的redis、数据库或者其他方式进行翻译
 * <p>
 * 如果使用自定义字典进行翻译，那么可以不重写该方法
 *
 * @param dict
 * @param value
 * @return
 */
@Override
public String dictToLabel(Dict dict,String value){
        String dictCode=dict.dictCode();//student_sex
        //通过redis、数据库或者其他方式进行翻译，例如 value 是1，那么返回男
        return"男";
        }

/**
 * 字典转换：字典标签转换到值
 * 你可在该方法中查询你的redis、数据库或者其他方式进行翻译
 * <p>
 * 如果使用自定义字典进行翻译，那么可以不重写该方法
 *
 * @param dict
 * @param label
 * @return
 */
@Override
public String dictToValue(Dict dict,String label){
        String dictCode=dict.dictCode();//student_sex
        //通过redis、数据库或者其他方式进行翻译，例如 label 是男，那么返回1
        return"1";
        }
```

