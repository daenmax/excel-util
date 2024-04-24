package cn.daenx.demo.excelUtil.controller;

import cn.daenx.demo.excelUtil.vo.ClassVo;
import cn.daenx.demo.excelUtil.vo.StudentVo;
import cn.daenx.lib.excelUtil.core.ExcelResult;
import cn.daenx.lib.excelUtil.core.ReadRetVo;
import cn.daenx.lib.excelUtil.utils.ExcelUtil;
import com.alibaba.excel.ExcelReader;
import com.alibaba.excel.ExcelWriter;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
@RestController
@RequestMapping("/test/sheets")
public class TestSheetsController {
    /**
     * 多sheet表-导入
     * http://127.0.0.1:8012/excel/test/sheets/importData
     */
    @PostMapping("/importData")
    public void importData(@RequestPart("file") MultipartFile file) throws IOException {
        ExcelReader reader = ExcelUtil.createImport(file.getInputStream());
        ReadRetVo<ClassVo> sheetA = ExcelUtil.readSheet("班级信息", ClassVo.class);
        ReadRetVo<StudentVo> sheetB = ExcelUtil.readSheet("学生信息", StudentVo.class);
        ExcelUtil.finishRead(reader, sheetA, sheetB);

        ExcelResult<ClassVo> classResult = ExcelUtil.transResult(sheetA);
        ExcelResult<StudentVo> studentResult = ExcelUtil.transResult(sheetB);

        List<ClassVo> classList = classResult.getList();
        List<StudentVo> studentList = studentResult.getList();
        System.out.println(classList);
        System.out.println(studentList);
        return;
    }

    /**
     * 多sheet表-导出
     * http://127.0.0.1:8012/excel/test/sheets/exportData
     */
    @PostMapping("/exportData")
    public void exportData(HttpServletResponse response) {
        ExcelWriter writer = ExcelUtil.createExport(response, "多sheet表测试");

        List<ClassVo> classList = new ArrayList<>();
        ClassVo class1 = new ClassVo();
        class1.setClassName("奋进班");
        class1.setClassNum("89");
        class1.setClassType("0");
        class1.setRemark("位于3楼");
        classList.add(class1);
        ClassVo class2 = new ClassVo();
        class2.setClassName("娇子班");
        class2.setClassNum("70");
        class2.setClassType("3");
        class2.setRemark("位于5楼");
        classList.add(class2);
        ExcelUtil.writeSheet(writer, "班级信息", classList, ClassVo.class);

        List<StudentVo> studentList = new ArrayList<>();
        StudentVo student1 = new StudentVo();
        student1.setStudentName("张三");
        student1.setStudentAge("21");
        student1.setStudentSex("1");
        student1.setStudentPost("0");
        student1.setRemark("成绩优秀");
        studentList.add(student1);
        StudentVo student2 = new StudentVo();
        student2.setStudentName("王苗苗");
        student2.setStudentAge("19");
        student2.setStudentSex("0");
        student2.setStudentPost("1");
        student2.setRemark("成绩一般");
        studentList.add(student2);
        ExcelUtil.writeSheet(writer, "学生信息", studentList, StudentVo.class);

        ExcelUtil.finishWrite(writer);
    }

    /**
     * 多sheet表-下载模板
     * http://127.0.0.1:8012/excel/test/sheets/template
     */
    @PostMapping("/template")
    public void template(HttpServletResponse response) {
        ExcelWriter writer = ExcelUtil.createExport(response, "多sheet表测试");
        ExcelUtil.writeSheet(writer, "班级信息", new ArrayList<>(), ClassVo.class);
        ExcelUtil.writeSheet(writer, "学生信息", new ArrayList<>(), StudentVo.class);
        ExcelUtil.finishWrite(writer);
    }
}
