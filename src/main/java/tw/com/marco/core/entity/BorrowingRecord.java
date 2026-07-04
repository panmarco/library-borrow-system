package tw.com.marco.core.entity;

import java.sql.Timestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import tw.com.marco.core.pojo.Core;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "borrowing_record")
public class BorrowingRecord extends Core {
	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer recordId;

	@Column(insertable = false, updatable = false)
	private Integer userId;
	
	@Column(insertable = false, updatable = false)
	private Integer inventoryId;

	// 資料庫default
	@Column(insertable = false, updatable = false)
	private Timestamp borrowingTime;

	private Timestamp returnTime;
}