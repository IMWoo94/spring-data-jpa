package study.datajpa.repository;

public interface NestedCloseProjections {

	// root select 절에 대해서 최적화
	String getUsername();

	// 중첩에 대해서는 최적화 x 전체 조회
	// left 조인으로 진행
	TeamInfo getTeam();

	interface TeamInfo {
		String getName();
	}
}
