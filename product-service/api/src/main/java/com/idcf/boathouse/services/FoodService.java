package com.idcf.boathouse.services;


import com.idcf.boathouse.JdbcUtils;
import com.idcf.boathouse.dao.BaseDao;
import com.idcf.boathouse.models.Food;
import com.idcf.boathouse.models.FoodCategory;
import com.idcf.boathouse.models.FoodPost;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class FoodService {

    public boolean VerifyName(String name) {
        Pattern Name_Pattern = Pattern.compile("[\\u4e00-\\u9fa5]{2,20}");
        Matcher matcher = Name_Pattern.matcher(name);
        return matcher.matches();
    }

    public void insertOrUpdateFood(FoodPost foodPost, InputStream is){

        List<Object> params = new ArrayList<Object>();
        String sql = "";
        params.add(foodPost.categoryId);
        params.add(foodPost.name);
        params.add(foodPost.price);
        params.add(foodPost.description);
        params.add(is);
        if(foodPost.id == null){
            sql = "insert into Food (categoryId, name, price, description, picture) values (?, ?, ?, ?, ?)";
        }else{
            sql = "update Food set categoryId = ?, name = ?, price= ?, description = ?, picture = ? where id = ?";
            params.add(foodPost.id);
        }
        BaseDao.exec(sql, params);
    }

    public void deleteFood(String id){
        String sql = "delete from Food where Id = ?";
        List<Object> params = new ArrayList<Object>();
        params.add(id);
        BaseDao.exec(sql, params);
    }

    public List<Map<String, Object>> listFood(Integer categoryId){
        StringBuffer sql = new StringBuffer("select * from Food");
        if(categoryId != null){
            sql.append(" where CategoryId = ");
            sql.append(categoryId.toString());
        }
        return BaseDao.exec(sql.toString());
    }

    public Food getFood(String id){
        String sql = "select * from Food where Id = ?";
        List<Object> params = new ArrayList<Object>();
        params.add(id);
        Food food;
        food = BaseDao.get(sql, params, Food.class);
        return food;
    }

    public boolean uploadFoodPicture(InputStream is, Integer id){
        String sql = "update Food set Picture= ? where Id = ?";
        List<Object> params = new ArrayList<Object>();
        params.add(is);
        params.add(id);
        BaseDao.exec(sql, params);
        return true;
    }
}
