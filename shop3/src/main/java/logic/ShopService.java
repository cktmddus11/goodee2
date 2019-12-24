package logic;

import java.util.Date;
import java.util.List;
import java.io.*;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import dao.BoardDao;
import dao.ItemDao;
import dao.SaleDao;
import dao.SaleItemDao;
import dao.UserDao;

@Service // @Component + service 기능
public class ShopService {
	@Autowired
	private ItemDao itemDao;
	@Autowired
	private UserDao userDao;
	@Autowired
	private SaleDao saleDao;
	@Autowired
	private SaleItemDao saleItemDao;
	@Autowired
	private BoardDao boardDao;
	
	
	public List<Item> getItemList() { // 가져온 정보를 리스트로 만들어서 리턴
		return itemDao.list();
	}

	public void itemCreate(Item item, HttpServletRequest request) {
		// 업로드 된 이미지 파일 존재 request업로드 경로 쓰려고
		if (item.getPicture() != null && !item.getPicture().isEmpty()) {
			// 파일 업로드 : 업로드된 파일의 내용을 파일에 저장
			uploadFileCreate(item.getPicture(), request, "item/img/");
			item.setPictureUrl(item.getPicture().getOriginalFilename());
			// 사진 url중 순수 사진 이름만 가져와서 item에 저장
		}
		itemDao.insert(item);

	}

	private void uploadFileCreate(MultipartFile picture, HttpServletRequest request, String path) {
		// picture : 업로드된 파일의 내용
		String orgFile = picture.getOriginalFilename();
		String uploadPath = request.getServletContext().getRealPath("/") + path;
		File fpath = new File(uploadPath);
		if (!fpath.exists()) {
			fpath.mkdirs();  // mkdirs 여러개의 푤더 생성
			// 한개 폴더 -> mkdir
		}
		try {
			// picture 업로드된 파일의 내용을 파일로 생성
			picture.transferTo(new File(uploadPath + orgFile));
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
	// 쌤코드 getItem이랑 같음
	public Item detailView(String id) {
		return itemDao.detailView(id);
	}

	public void itemUpdate(Item item, HttpServletRequest request) {
		// 수정된 이미지 존재
		if (item.getPicture() != null && !item.getPicture().isEmpty()) {
			// 파일 업로드 : 업로드된 파일의 내용을 파일에 저장
			uploadFileCreate(item.getPicture(), request, "item/img/");
			item.setPictureUrl(item.getPicture().getOriginalFilename());
			// 사진 url중 순수 사진 이름만 가져와서 item에 저장
		}
		itemDao.update(item);

	}

	public void deleteItem(int id) {
		itemDao.delete(id);
		
	}

	public void userInsert(User user) {
		userDao.userinsert(user);
	}

	public User getUser(String userid) {
		return userDao.selectOne(userid);
	}

	public Item getItem(String id) { // detailview랑 같음
		return itemDao.detailView(id);
	}

	public Sale checkend(User loginUser, Cart cart) {
		// 주문자 정보를 sale테이블에 저장
		Sale sale = new Sale();
		// sale 테이블의 saleid값 + 1
		sale.setSaleid(saleDao.getMaxSaleId()); 
		sale.setUser(loginUser); // 구매자 정보
		sale.setUserid(loginUser.getUserid()); // 구매자 아이디
		sale.setUpdatetime(new Date()); // 주문시간
		saleDao.insert(sale);
		// 주문 상품 정보를 cart 에서 조회
		List<ItemSet> itemList = cart.getItemSetList();
		int i = 0;
		for(ItemSet is : itemList) {
			int saleItemId = ++i; 
			SaleItem saleItem = new SaleItem(sale.getSaleid(), saleItemId, is);
			sale.getItemList().add(saleItem);
			saleItemDao.insert(saleItem);
		}
		return sale;
		
	}

	public List<Sale> salelist(String id) {
		return saleDao.list(id); // 사용자 id
	}

	public List<SaleItem> saleItemList(int saleid) {
		return saleItemDao.list(saleid); // saleid 주문번호
	}

	public void userUpdate(User user) {
		userDao.update(user);
		
	}

	public void userDelete(String userid) {
		userDao.delete(userid);
	}

	public int boardcount(String searchtype, String searchcontent) {
		return boardDao.count(searchtype, searchcontent);
	}

	public List<Board> boardlist(Integer pageNum, int limit, String searchtype, String searchcontent) {
		return boardDao.list(pageNum, limit, searchtype, searchcontent);
	}

	public void boardWrite(Board board, HttpServletRequest request) {
		// 첨부파일이 존재하는 경우
		if(board.getFile1() != null && !board.getFile1().isEmpty()) {
			uploadFileCreate(board.getFile1(), request,  "board/file/");
			// 업로드된 파일 이름 설정
			board.setFileurl(board.getFile1().getOriginalFilename());
		}
		// 현재 등록된 게시물 번호 최대값
		int max = boardDao.maxnum();
		board.setNum(++max);
		board.setGrp(max);
		boardDao.insert(board);
	}

	public Board getBoard(Integer num, HttpServletRequest request) {
		if(request.getRequestURI().contains("detail.shop")) { // 경로가 저거를 포함하고 있을 때 
			boardDao.readcntadd(num); // 조회수 증가
		}
		return boardDao.selectOne(num);
	}

	public void boardReply(Board board) {
		// grpstep 답글의 순서 최근에 단 답글이 숫자가 작음 
		// grpstep + 1 증가
		boardDao.grpstep(board.getGrp(), board.getGrpstep()); 
		// 답변글 등록
		board.setNum(boardDao.maxnum() + 1);
		board.setGrplevel(board.getGrplevel()+1);
		board.setGrpstep(board.getGrpstep()+1);
		boardDao.insert(board);
	}
	
	// 답글 달때 제목 RE계속 생기는 버그때문에 하는거
	public Board getBoard(int num) {
		return boardDao.selectOne(num);
	}

	public String getPass(int num) {
		return boardDao.getPass(num);
	}

	public void boardUpdate(Board board, HttpServletRequest request) {
		// 수정된 이미지 존재
		if (board.getFile1() != null && !board.getFile1().isEmpty()) {
			// 파일 업로드 : 업로드된 파일의 내용을 파일에 저장
			uploadFileCreate(board.getFile1(), request, "board/img/");
			board.setFileurl(board.getFile1().getOriginalFilename());
			// 사진 url중 순수 사진 이름만 가져와서 item에 저장
		}
		boardDao.boardupdate(board);

	}

	public void boardDelete(int num) {
		boardDao.boardDelete(num);
		
	}
	

}
