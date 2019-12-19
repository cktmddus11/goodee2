package logic;

import java.util.ArrayList;
import java.util.List;

public class Cart {
	private List<ItemSet> itemSetList = new ArrayList<ItemSet>();
	public List<ItemSet> getItemSetList(){
		return itemSetList;
	}
	public void push(ItemSet itemSet) {
		for(ItemSet i : itemSetList) {
				Item item = i.getItem();
				Item additem = itemSet.getItem();
				int quantity = i.getQuantity();
				int addquantity = itemSet.getQuantity();
				if(item.getId().equals(additem.getId())) { // 아이디가 같은 게 있으면
					i.setQuantity(quantity + addquantity); // 있는 수량에 추가한 상품의 수량 추가
					return;
				}
		}
			itemSetList.add(itemSet);
	}
	public long getTotal() {
		long sum = 0;
		for(ItemSet i : itemSetList) {
			Item item = i.getItem();
			int cnt = i.getQuantity();
			sum += item.getPrice() * cnt;
		}
		return sum;
	}

}
