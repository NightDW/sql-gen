package com.laidw.sql.gen.controller;

import com.laidw.sql.gen.entity.Database;
import com.laidw.sql.gen.config.DatabaseProperties;
import com.laidw.sql.gen.exception.SqlGenException;
import com.laidw.sql.gen.ro.ChangeDatabaseRO;
import com.laidw.sql.gen.ro.SqlGenRO;
import com.laidw.sql.gen.service.SqlGenService;
import com.laidw.sql.gen.strategy.DatabaseLoader;
import com.laidw.sql.gen.util.DemoUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.sql.SQLException;
import java.util.List;

/**
 * Description of class {@link SqlGenController}.
 *
 * @author NightDW 2023/8/29 15:31
 */
@Controller
public class SqlGenController {

    @Autowired
    private SqlGenService sqlGenService;

    @Autowired
    private List<DatabaseLoader> databaseLoaders;

    @Autowired
    private DatabaseProperties databaseProperties;

    @GetMapping("/")
    public ModelAndView index(HttpServletRequest request) {
        ModelAndView mav = new ModelAndView("index");
        mav.addObject("data", sqlGenService.render(getDatabase(request)));
        mav.addObject("db", getDatabase(request));
        return mav;
    }

    @PostMapping("/submit")
    public ModelAndView submit(HttpServletRequest request, @Valid SqlGenRO sqlGenRO) {
        ModelAndView mav = new ModelAndView("index");
        mav.addObject("data", sqlGenService.render(sqlGenRO, getDatabase(request)));
        mav.addObject("db", getDatabase(request));
        return mav;
    }

    @GetMapping("/database")
    public ModelAndView database() {
        ModelAndView mav = new ModelAndView("database");
        mav.addObject("properties", databaseProperties);
        return mav;
    }

    @PostMapping("/change")
    public ModelAndView change(HttpServletRequest request, @Valid ChangeDatabaseRO changeDatabaseRO) {
        Database db = null;
        for (DatabaseLoader databaseLoader : databaseLoaders) {
            if (databaseLoader.support(changeDatabaseRO.getUrl())) {
                try {
                    db = databaseLoader.get(changeDatabaseRO.getUrl(), changeDatabaseRO.getUser(), changeDatabaseRO.getPassword());
                } catch (SQLException e) {
                    throw new SqlGenException("读取数据库失败：" + e.getMessage());
                }
                break;
            }
        }

        if (db == null) {
            throw new SqlGenException("不支持的url连接");
        }

        setDatabase(request, db);
        return index(request);
    }

    @PostMapping("/generate")
    public ModelAndView generate(HttpServletRequest request, @Valid SqlGenRO sqlGenRO) {
        ModelAndView mav = new ModelAndView("result");
        mav.addObject("sql", sqlGenService.generateSql(sqlGenRO, getDatabase(request)));
        mav.addObject("rovo", sqlGenService.generateClass(sqlGenRO, getDatabase(request)));
        return mav;
    }

    private static Database getDatabase(HttpServletRequest request) {
        Database db = (Database) request.getSession().getAttribute("db");
        if (db == null) {
            setDatabase(request, db = DemoUtil.database);
        }
        return db;
    }

    private static void setDatabase(HttpServletRequest request, Database db) {
        request.getSession().setAttribute("db", db);
    }
}
