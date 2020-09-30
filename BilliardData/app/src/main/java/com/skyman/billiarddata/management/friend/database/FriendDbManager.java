package com.skyman.billiarddata.management.friend.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.widget.Toast;

import com.skyman.billiarddata.developer.DeveloperManager;
import com.skyman.billiarddata.management.billiard.data.BilliardData;
import com.skyman.billiarddata.management.billiard.database.BilliardTableSetting;
import com.skyman.billiarddata.management.friend.data.FriendData;
import com.skyman.billiarddata.management.user.database.UserDbHelper;
import com.skyman.billiarddata.management.user.database.UserTableSetting;

import java.util.ArrayList;

public class FriendDbManager {

    // value : SQLiteOpenHelper 를 extends 한 FriendDbHelper 객체 선언
    private FriendDbHelper friendDbHelper;

    // value : FriendDbManager 를 생성한 activity 의 Context
    private Context targetContext;

    // value : init_db 실행 여부 확인
    private boolean initDb;

    // constructor
    public FriendDbManager(Context targetContext) {
        this.friendDbHelper = null;
        this.targetContext = targetContext;
        this.initDb = false;
    }


    /*                                      public method
     * =============================================================================================
     * =============================================================================================
     * */

    /* method : getter, setter */
    public boolean isInitDb() {
        return initDb;
    }

    /* method 0 : init, SQLite DB open helper 를 이용하여 초기화 */
    public void init_db() {
        /*
         * =========================================================================================
         * project_blue.db 에 billiard, user, friend 테이블의 존재 여부 확인한다.
         * 없으면 project_blue.db 생성 후 billiard, user, friend 테이블을 생성하고,
         * project_blue.db 를 open 한다.
         * =========================================================================================
         * */
        friendDbHelper = new FriendDbHelper(targetContext);
        initDb = true;
        DeveloperManager.displayLog("[DbM] FriendDbManager", "[init_db] The method is complete. - initDb : " + initDb);
    }   // End of 'init_db'

    /* method 1 : insert, SQLite DB open helper 를 이용하여 해당 테이블에 정보를 insert 한다. */
    public long save_content(long userId, String name, int gameRecordWin, int gameRecordLoss, String recentPlayDate, int totalPlayTime, int totalCost) {
        /*
         * =====================================================
         * friend table insert query
         * -    데이터를 매개변수로 받아서 모든 값을 입력 받은지 확인한다.
         *      그리고 ContentValues 의 객체에 내용 setting 을 한다.
         *      friendDbHelper 를 통해 writeable 으로 받아온 SQLiteDatabase 를 이용하여
         *      project_blue.db 의 friend 테이블에 해당 내용을 insert 한다.
         * - 입력 순서
         *      1. userID
         *      2. name
         *      3. gameRecordWin
         *      4. gameRecordLoss
         *      5. recentPlayDate
         *      6. totalPlayTime
         *      7. totalCost
         * - return
         *      1. '-2' : 입력 조건이 만족하지 않음
         *      2. '-1' : 데이터베이스 insert 실패
         *      3. '0' : 기본 값
         *      4. '0' 보다 큰 수 : insert 된 곳의 행 번호
         * =====================================================
         * */
        DeveloperManager.displayLog("[DbM] FriendDbManager", "[save_content] The method is executing ............");

        // SQLiteDatabase : write database - billiardDbHelper 를 통해 write 용으로 가져오기
        SQLiteDatabase writeDb = friendDbHelper.getWritableDatabase();

        // long : 이 메소드의 결과를 저장하기 위한 변수 선언, 행 번호
        long newRowId = 0;

        // check : 매개변수의 내용 중에 빈 곳이 없나 검사
        if ((userId > 0) &&
                !name.equals("") &&
                (gameRecordWin >= 0) &&
                (gameRecordLoss >= 0) &&
                !recentPlayDate.equals("") &&
                (totalPlayTime >= 0) &&
                (totalCost >= 0)
        ) {
            DeveloperManager.displayLog("[DbM] FriendDbManager", "[save_content] 모든 조건이 통과되었습니다.");
            // ContentValues : 매개변수의 내용을 insertValues 에 셋팅하기
            ContentValues insertValues = new ContentValues();
            insertValues.put(FriendTableSetting.Entry.COLUMN_NAME_USER_ID, userId);                     // 1. user id
            insertValues.put(FriendTableSetting.Entry.COLUMN_NAME_NAME, name);                          // 2. name
            insertValues.put(FriendTableSetting.Entry.COLUMN_NAME_GAME_RECORD_WIN, gameRecordWin);      // 3. game record win
            insertValues.put(FriendTableSetting.Entry.COLUMN_NAME_GAME_RECORD_LOSS, gameRecordLoss);    // 4. game record loss
            insertValues.put(FriendTableSetting.Entry.COLUMN_NAME_RECENT_PLAY_DATE, recentPlayDate);    // 5. recent play date
            insertValues.put(FriendTableSetting.Entry.COLUMN_NAME_TOTAL_PLAY_TIME, totalPlayTime);      // 6. total play time
            insertValues.put(FriendTableSetting.Entry.COLUMN_NAME_TOTAL_COST, totalCost);               // 7. total cost

            /*  해당 friend 테이블에 내용 insert
                nullColumnHack 이 'null'로 지정 되었다면?       values 객체의 어떤 열에 값이 없으면 지금 내용을 insert 안함.
                               이 '열 이름'이 지정 되었다면?    해당 열에 값이 없으면 null 값을 넣는다.*/

            // newRowId는 데이터베이스에 insert 가 실패하면 '-1'을 반환하고, 성공하면 해당 '행 번호'를 반환한다.
            newRowId = writeDb.insert(FriendTableSetting.Entry.TABLE_NAME, null, insertValues);

            // check : 데이터베이스 입력이 실패, 성공 했는지 구분하여
            if (newRowId == -1) {
                // 데이터 insert 실패
                DeveloperManager.displayLog("[DbM] FriendDbManager", "[save_content] DB 저장 실패 : " + newRowId + " 값을 리턴합니다.");
                return newRowId;
            } else {
                // 데이터 insert 성공
                DeveloperManager.displayLog("[DbM] FriendDbManager", "[save_content] " + newRowId + " 번째 입력이 성공하였습니다.");
            }
        } else {
            DeveloperManager.displayLog("[DbM] FriendDbManager", "[save_content] 입력 된 값이 조건에 만족하지 않습니다. : " + newRowId + " 값을 리턴합니다.");
            return -2;
        }

        // SQLiteDatabase : close
        writeDb.close();

        DeveloperManager.displayLog("[DbM] FriendDbManager", "[save_content] The method is complete");
        return newRowId;
    }       // End of 'save_contents'


    /* method : load, SQLite DB Helper를 이용하여 해당 테이블의 정보를 가져온다. */
    public ArrayList<FriendData> load_contents() {
        /*
         * =====================================================
         * friend table select query
         * -    friendDbHelper 를 통해 readable 으로 받아온 SQLiteDatabase 를 이용하여
         *      project_blue.db 의 friend 테이블의 모든 내용을 읽어온다.
         *
         * - return : 읽어온 내용이 담기 ArrayList<FriendData>
         * =====================================================
         * */
        DeveloperManager.displayLog("[DbM] FriendDbManager", "[load_contents] The method is executing ............");

        // SQLiteDatabase : read database - billiardDbHelper 를 통해 read 용으로 가져오기
        SQLiteDatabase readDb = friendDbHelper.getReadableDatabase();

        // Cursor : 해당 쿼리문으로 읽어온 테이블 내용을 Cursor 객체를 통해 읽을 수 있도록 하기
        Cursor cursor = readDb.rawQuery(FriendTableSetting.SQL_SELECT_TABLE_ALL_ITEM, null);

        // ArrayList<friendData> : FriendData 객체를 담을 ArrayList 객체 생성 - 한 행을 FriendData 객체에 담는다.
        ArrayList<FriendData> friendDataArrayList = new ArrayList<>();

        // cycle : Cursor 객체의 다음 내용이 있을 때 마다 체크하여 모두 friendDataArrayList 에 담는다.
        while (cursor.moveToNext()) {
            // FriendData : cursor 에서 읽어 온 내용(한 행)을 friendData 에 담는다.
            FriendData friendData = new FriendData();
            friendData.setId(cursor.getLong(0));
            friendData.setUserId(cursor.getLong(1));
            friendData.setName(cursor.getString(2));
            friendData.setGameRecordWin(cursor.getInt(3));
            friendData.setGameRecordLoss(cursor.getInt(4));
            friendData.setRecentPlayDate(cursor.getString(5));
            friendData.setTotalPlayTime(cursor.getInt(6));
            friendData.setTotalCost(cursor.getInt(7));
            DeveloperManager.displayToFriendData("[DbM] FriendDbManager", friendData);

            // ArrayList<friendData> : 위 의 내용을 배열 형태로 담아둔다.
            friendDataArrayList.add(friendData);
        }

        // SQLiteDatabase : close
        readDb.close();

        DeveloperManager.displayLog("[DbM] FriendDbManager", "[load_contents] The method is complete!");
        return friendDataArrayList;
    }       // End of 'load_contents'

    /* method : load, SQLite DB Helper 를 이용하여 해당 테이블의 정보를 가져온다. */
    public FriendData load_contents(long id) {
        /*
         * =====================================================
         * friend table select query
         * -    friendDbHelper 를 통해 readable 으로 받아온 SQLiteDatabase 를 이용하여
         *      project_blue.db 의 friend 테이블의 모든 내용을 읽어온다.
         * - 매개변수
         *      0. id
         * - return : 읽어온 내용이 담기 ArrayList<FriendData>
         * =====================================================
         * */
        DeveloperManager.displayLog("[DbM] FriendDbManager", "[load_contents] The method is executing ............");

        // SQLiteDatabase : read database - billiardDbHelper 를 통해 read 용으로 가져오기
        SQLiteDatabase readDb = friendDbHelper.getReadableDatabase();

        // Cursor : 해당 쿼리문으로 읽어온 테이블 내용을 Cursor 객체를 통해 읽을 수 있도록 하기
        Cursor cursor = readDb.rawQuery(FriendTableSetting.SQL_SELECT_TABLE_ALL_ITEM, null);


        // FriendData
        FriendData friendData = new FriendData();

        // cycle : Cursor 객체의 다음 내용이 있을 때 마다 체크하여 모두 friendData 에 담는다.
        if (cursor.moveToFirst()) {
            // FriendData : cursor 에서 읽어 온 내용(한 행)을 friendData 에 담는다.
            friendData.setId(cursor.getLong(0));
            friendData.setUserId(cursor.getLong(1));
            friendData.setName(cursor.getString(2));
            friendData.setGameRecordWin(cursor.getInt(3));
            friendData.setGameRecordLoss(cursor.getInt(4));
            friendData.setRecentPlayDate(cursor.getString(5));
            friendData.setTotalPlayTime(cursor.getInt(6));
            friendData.setTotalCost(cursor.getInt(7));
            DeveloperManager.displayToFriendData("[DbM] FriendDbManager", friendData);
        }

        // SQLiteDatabase : close
        readDb.close();

        DeveloperManager.displayLog("[DbM] FriendDbManager", "[load_contents] The method is complete!");
        return friendData;
    }       // End of 'load_contents'

    /* method : update - SQLite DB Open Helper 를 이용하여 user 테이블에 있던 내용을 update 한다. */
    public int update_content(long id, int gameRecordWin, int gameRecordLoss, String recentPlayDate, int totalPlayTime, int totalCost) {
        /*
         * =========================================================================================
         * friend table insert query
         * -    데이터를 매개변수로 받아서 모든 값을 입력 받은지 확인한다.
         *      그리고 ContentValues 의 객체에 내용 setting 을 한다.
         *      friendDbHelper 를 통해 writeable 으로 받아온 SQLiteDatabase 를 이용하여
         *      project_blue.db 의 friend 테이블에 해당 내용을 update 한다.
         * - 매개 변수
         *      0. id
         *      3. game record win
         *      4. game record loss
         *      5. recent play date
         *      6. total play time
         *      7. total cost
         * - return : update 성공 여부
         * =========================================================================================
         * */
        DeveloperManager.displayLog("[DbM] FriendDbManager", "[update_content] The method is executing ............");

        // SQLiteDatabase : userDbHelper 를 이용하여 write 용으로 가져오기
        SQLiteDatabase updateDb = friendDbHelper.getWritableDatabase();

        // ContentValues :
        ContentValues updateValue = new ContentValues();
        updateValue.put(FriendTableSetting.Entry.COLUMN_NAME_GAME_RECORD_WIN, gameRecordWin);
        updateValue.put(FriendTableSetting.Entry.COLUMN_NAME_GAME_RECORD_LOSS, gameRecordLoss);
        updateValue.put(FriendTableSetting.Entry.COLUMN_NAME_RECENT_PLAY_DATE, recentPlayDate);
        updateValue.put(FriendTableSetting.Entry.COLUMN_NAME_TOTAL_PLAY_TIME, totalPlayTime);
        updateValue.put(FriendTableSetting.Entry.COLUMN_NAME_TOTAL_COST, totalCost);

        // SQLiteDatabase : update query 수행
        int updateResult = updateDb.update(FriendTableSetting.Entry.TABLE_NAME, updateValue, FriendTableSetting.Entry._ID + "=" + id, null);

        DeveloperManager.displayLog("[DbM] FriendDbManager", "[update_content] The method is complete!");
        return updateResult;
    }       // End of 'update_content'

    /* method : delete - SQLite DB Open Helper 를 이용하여 friend 테이블에 있던 내용을 모두 delete 한다.*/
    public int delete_contents() {
        /*
         * =========================================================================================
         * user table select query
         * -    userDbHelper 를 통해 writable 으로 받아온 SQLiteDatabase 를 이용하여
         *      project_blue.db 에서 user 테이블의 모든 내용을 delete 한다.
         * - return : delete 성공 여부
         * =========================================================================================
         * */
        DeveloperManager.displayLog("[DbM] FriendDbManager", "[delete_contents] The method is executing ............");

        // SQLiteDatabase : write database - userDbHelper 를 write 용으로 가져오기
        SQLiteDatabase deleteDb = friendDbHelper.getWritableDatabase();

        // SQLiteDatabase : 모든 내용 삭제
        int deleteResult = deleteDb.delete(FriendTableSetting.Entry.TABLE_NAME, null, null);

        // SQLiteDatabase : DB close
        deleteDb.close();
        DeveloperManager.displayLog("[DbM] FriendDbManager", "[delete_contents] The method is complete");

        // return : delete 문 실행 결과 리턴
        return deleteResult;
    }       // End of 'delete_contents'

    /* method : delete - SQLite DB Open Helper 를 이용하여 friend 테이블에 있던 내용을 모두 delete 한다.*/
    public int delete_content(long id) {
        /*
         * =========================================================================================
         * user table select query
         * -    userDbHelper 를 통해 writable 으로 받아온 SQLiteDatabase 를 이용하여
         *      project_blue.db 에서 user 테이블의 모든 내용을 delete 한다.
         * - 매개변수
         *      0. id
         * - return : delete 성공 여부
         * =========================================================================================
         * */
        DeveloperManager.displayLog("[DbM] FriendDbManager", "[delete_content] The method is executing ............");

        // SQLiteDatabase : write database - userDbHelper 를 write 용으로 가져오기
        SQLiteDatabase deleteDb = friendDbHelper.getWritableDatabase();

        // SQLiteDatabase : 모든 내용 삭제
        int deleteResult = deleteDb.delete(FriendTableSetting.Entry.TABLE_NAME, FriendTableSetting.Entry._ID + "=" + id, null);

        // SQLiteDatabase : DB close
        deleteDb.close();

        DeveloperManager.displayLog("[DbM] FriendDbManager", "[delete_content] The method is complete");
        return deleteResult;
    }

    /* method : friendDbHelper close */
    public void closeFriendDbHelper() {
        if (initDb) {
            friendDbHelper.close();
            DeveloperManager.displayLog("[DbM] FriendDbManager", "[closeFriendDbHelper] FriendDbManager is closed.");
        } else {
            DeveloperManager.displayLog("[DbM] FriendDbManager", "[closeFriendDbHelper] FriendDbManager 가 초기화되지 않았습니다.");
        }
    }


    /*                                      private method
     * =============================================================================================
     * =============================================================================================
     * */

    /* method : display, toast 메시지 출력 */
    private void toastHandler(String content) {
        Toast myToast = Toast.makeText(targetContext, content, Toast.LENGTH_SHORT);
        myToast.show();
    }
}
