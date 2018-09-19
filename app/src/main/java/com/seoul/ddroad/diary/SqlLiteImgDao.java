package com.seoul.ddroad.diary;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.widget.Toast;

import java.util.HashMap;

public class SqlLiteImgDao {
    private SqlLiteOpenImgHelper helper;
    private SQLiteDatabase database;  // database를 다루기 위한 SQLiteDatabase 객체 생성
    private String diaryDatabaseName = "ddroad.db"; //데이터베이스 이름

    public SqlLiteImgDao(Context context){

        helper = new SqlLiteOpenImgHelper(context, // 현재 화면의 context
                diaryDatabaseName, // 파일명
                null, // 커서 팩토리
                1); // 버전 번호

    }

    public void insertDiaryImg(int diaryId, String imgDir){
        database = helper.getWritableDatabase();
        if(database != null){
            String sql = "insert into diaryimg(diaryId, imgDir) values(?, ?)";
            Object[] params = { diaryId, imgDir};
            database.execSQL(sql, params);
        }
    }

    public void deleteDiaryImg(int diaryId){
        database = helper.getWritableDatabase();
        if(database != null){
            String sql = "Delete from diaryimg where diaryId = ?";
            Object[] params = { diaryId};
            database.execSQL(sql, params);
        }
    }

    public HashMap<String,Object> selectDiaryImg(int diaryId){
        HashMap<String,Object> map = null;
        database = helper.getWritableDatabase();
        if(database != null){
            String sql = "select diaryId, imgDir from diaryimg where diaryId = "+diaryId+";";
            Cursor result = database.rawQuery(sql, null);

            if(result.moveToFirst()){ //커서가 처음지점이 값이있으면,
                int id = result.getInt(0);
                String title = result.getString(1); // 두번째 속성

                map.put("diaryId",diaryId);
                map.put("imgDir",title);
            }
            result.close();
        }

        return map;
    }
}
