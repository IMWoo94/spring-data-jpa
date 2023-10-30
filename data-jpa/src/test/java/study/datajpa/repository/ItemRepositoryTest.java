package study.datajpa.repository;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import study.datajpa.entity.Item;

@SpringBootTest
class ItemRepositoryTest {

	@Autowired
	ItemRepository itemRepository;

	@Test
	void save() {
		Item item = new Item(1L);
		// 식별자가 있는 상태로 진행했기 때문에, persist 가 아닌 merge 가 발생한다.
		// 식별자를 직접 입력하는 경우에는 Persistable 를 implements 를 해서 isNew
		// 신규 인지에 대한 값을 재정의 해줄 것
		Item save = itemRepository.save(item);

		// Assertions.assertThat(save).isNotEqualTo(item);
		Assertions.assertThat(save).isEqualTo(item);
	}

}