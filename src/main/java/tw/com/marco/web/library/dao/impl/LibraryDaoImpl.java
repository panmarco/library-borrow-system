package tw.com.marco.web.library.dao.impl;

import java.util.List;

import org.hibernate.Session;
import org.springframework.stereotype.Repository;

import jakarta.persistence.ParameterMode;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.StoredProcedureQuery;
import tw.com.marco.core.entity.BorrowingRecord;
import tw.com.marco.core.entity.Inventory;
import tw.com.marco.web.library.dao.LibraryDao;

@Repository
public class LibraryDaoImpl implements LibraryDao {
	@PersistenceContext
	private Session session;

	// 新增借閱紀錄
	@Override
	public int insertBorrowingRecord(Integer userId, Integer inventoryId) {
		StoredProcedureQuery query = session.createStoredProcedureQuery("sp_insert_borrowing_record");

		query.registerStoredProcedureParameter("p_user_id", Integer.class, ParameterMode.IN);
		query.registerStoredProcedureParameter("p_inventory_id", Integer.class, ParameterMode.IN);
		query.setParameter("p_user_id", userId);
		query.setParameter("p_inventory_id", inventoryId);

		return query.executeUpdate();
	}

	// 更新庫存書籍狀態
	@Override
	public int updateInventoryStatus(Integer inventoryId, String status) {
		StoredProcedureQuery query = session.createStoredProcedureQuery("sp_update_inventory_status");

		query.registerStoredProcedureParameter("p_inventory_id", Integer.class, ParameterMode.IN);
		query.registerStoredProcedureParameter("p_status", String.class, ParameterMode.IN);
		query.setParameter("p_inventory_id", inventoryId);
		query.setParameter("p_status", status);

		return query.executeUpdate();
	}

	// 更新還書時間
	@Override
	public int updateReturnTime(Integer userId, Integer inventoryId) {
		StoredProcedureQuery query = session.createStoredProcedureQuery("sp_update_return_time");

		query.registerStoredProcedureParameter("p_user_id", Integer.class, ParameterMode.IN);
		query.registerStoredProcedureParameter("p_inventory_id", Integer.class, ParameterMode.IN);
		query.setParameter("p_user_id", userId);
		query.setParameter("p_inventory_id", inventoryId);

		return query.executeUpdate();
	}

	// 查詢所有庫存
	@Override
	public List<Inventory> selectAllInventory() {
		StoredProcedureQuery query = session.createStoredProcedureQuery("sp_select_all_inventory");
		
		List<Object[]> rows = query.getResultList();
	    List<Inventory> resultList = new java.util.ArrayList<>();
		for (Object[] row : rows) {
	        Inventory inv = new Inventory();
	        inv.setInventoryId((Integer) row[0]);
	        inv.setIsbn((String) row[1]);
	        // java.time.LocalDateTime 接收，再轉成 java.sql.Timestamp
	        inv.setStoreTime(java.sql.Timestamp.valueOf((java.time.LocalDateTime) row[2]));
	        inv.setStatus((String) row[3]);
	        // 手動塞值進@Transient中
	        inv.setBookName((String) row[4]);
	        inv.setAuthor((String) row[5]);
	        inv.setIntroduction((String) row[6]);
	        
	        resultList.add(inv);
	    }
	    return resultList;
	}
	
	// 查詢單一書籍狀態
	@Override
	public String selectInventoryStatus(Integer inventoryId) {
		StoredProcedureQuery query = session.createStoredProcedureQuery("sp_select_inventory_status");

		query.registerStoredProcedureParameter("p_inventory_id", Integer.class, ParameterMode.IN);
		query.registerStoredProcedureParameter("p_status", String.class, ParameterMode.OUT);
		query.setParameter("p_inventory_id", inventoryId);
		
		query.execute();

		return (String) query.getOutputParameterValue("p_status");
	}

	// 查詢所有借閱紀錄
	@Override
	public List<BorrowingRecord> selectBorrowingRecordsByUserId(Integer userId) {
		StoredProcedureQuery query = session.createStoredProcedureQuery("sp_select_borrowing_record_user_id",
				BorrowingRecord.class);
		query.registerStoredProcedureParameter("p_user_id", Integer.class, ParameterMode.IN);
		query.setParameter("p_user_id", userId);
		
		return query.getResultList();
	}

	@Override
	public Integer selectBorrowingRecordUserIdByInventoryId(Integer inventoryId) {
		StoredProcedureQuery query = session.createStoredProcedureQuery("sp_select_borrowing_record_user_id_by_inventory_id");

		query.registerStoredProcedureParameter("p_inventory_id", Integer.class, ParameterMode.IN);
		query.registerStoredProcedureParameter("p_user_id", Integer.class, ParameterMode.OUT);
		query.setParameter("p_inventory_id", inventoryId);
		
		query.execute();

		return (Integer) query.getOutputParameterValue("p_user_id");
	}
}