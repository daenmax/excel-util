package cn.daenx.demo.excelUtil.controller;

import cn.daenx.demo.excelUtil.vo.StudentVo;
import cn.daenx.lib.excelUtil.core.ExcelResult;
import cn.daenx.lib.excelUtil.utils.ExcelUtil;
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
@RequestMapping("/test/sheet")
public class TestSheetController {
    /**
     * 单sheet表-导入
     * http://127.0.0.1:8012/excel/test/sheet/importData
     */
    @PostMapping("/importData")
    public void importData(@RequestPart("file") MultipartFile file) throws IOException {
        ExcelResult<StudentVo> studentResult = ExcelUtil.importExcel(file.getInputStream(), StudentVo.class);
        List<StudentVo> studentList = studentResult.getList();
        System.out.println(studentList);
        return;
    }

    /**
     * 单sheet表-导出
     * http://127.0.0.1:8012/excel/test/sheet/exportData
     */
    @PostMapping("/exportData")
    public void exportData(HttpServletResponse response) {
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

        ExcelUtil.exportXlsx(response, "单sheet表测试", "学生信息", studentList, StudentVo.class);
    }

    /**
     * 单sheet表-下载模板
     * http://127.0.0.1:8012/excel/test/sheet/template
     */
    @PostMapping("/template")
    public void template(HttpServletResponse response) {
        ExcelUtil.exportXlsx(response, "单sheet表测试", "学生信息", new ArrayList<>(), StudentVo.class);
    }
}
