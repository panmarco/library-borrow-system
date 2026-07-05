package tw.com.marco.core.pojo;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import tw.com.marco.core.entity.BorrowingRecord;
import tw.com.marco.core.entity.Inventory;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AllData extends Core {
	private static final long serialVersionUID = 1L;
	private List<Inventory> inventories;
	private List<BorrowingRecord> records;
}
