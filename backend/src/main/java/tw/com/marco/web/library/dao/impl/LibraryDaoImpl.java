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

	// 查詢所有在庫書籍
	@Override
	public List<Inventory> selectInventoryByStatus() {
		StoredProcedureQuery query = session.createStoredProcedureQuery("sp_select_inventory_by_status",
				Inventory.class);
		return query.getResultList();
	}

	@Override
	public int updateInventoryStatus(Integer inventoryId, String status) {
		StoredProcedureQuery query = session.createStoredProcedureQuery("sp_update_inventory_status");

		query.registerStoredProcedureParameter("p_inventory_id", Integer.class, ParameterMode.IN);
		query.registerStoredProcedureParameter("p_status", String.class, ParameterMode.IN);

		query.setParameter("p_inventory_id", inventoryId);
		query.setParameter("p_status", status);

		return query.executeUpdate();
	}

	@Override
	public int insertBorrowingRecord(Integer userId, Integer inventoryId) {
		StoredProcedureQuery query = session.createStoredProcedureQuery("sp_insert_borrowing_record");

		query.registerStoredProcedureParameter("p_user_id", Integer.class, ParameterMode.IN);
		query.registerStoredProcedureParameter("p_inventory_id", Integer.class, ParameterMode.IN);

		query.setParameter("p_user_id", userId);
		query.setParameter("p_inventory_id", inventoryId);

		return query.executeUpdate();
	}

	@Override
	public List<BorrowingRecord> selectAllBorrowingRecords() {
		StoredProcedureQuery query = session.createStoredProcedureQuery("sp_select_all_borrowing_record",
				BorrowingRecord.class);
		return query.getResultList();
	}

	@Override
	public int updateReturnTime(Integer userId, Integer inventoryId) {
		StoredProcedureQuery query = session.createStoredProcedureQuery("sp_update_return_time");

		query.registerStoredProcedureParameter("p_user_id", Integer.class, ParameterMode.IN);
		query.registerStoredProcedureParameter("p_inventory_id", Integer.class, ParameterMode.IN);

		query.setParameter("p_user_id", userId);
		query.setParameter("p_inventory_id", inventoryId);

		return query.executeUpdate();
	}

}