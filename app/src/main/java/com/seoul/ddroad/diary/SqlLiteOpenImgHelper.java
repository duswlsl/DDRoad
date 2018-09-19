package com.seoul.ddroad.diary;


import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


public class SqlLiteOpenImgHelper extends SQLiteOpenHelper {
    private String diaryTableName = "diaryimg"; //테이블 이름
    public SqlLiteOpenImgHelper(Context context, String name,
                                SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
        // TODO Auto-generated constructor stub

    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // TODO Auto-generated method stub
        // SQLiteOpenHelper 가 최초 실행 되었을 때
        String sql = "CREATE  TABLE IF NOT EXISTS " + diaryTableName + "(diaryId integer , imgDir text)"; //테이블 확인후 생성

        db.execSQL(sql);
    }


    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        String sql = "drop table if exists "+diaryTableName;
        db.execSQL(sql);

        onCreate(db); // 테이블을 지웠으므로 다시 테이블을 만들어주는 과정
    }
}
