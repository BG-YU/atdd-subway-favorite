package nextstep.subway.acceptance;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import static nextstep.subway.acceptance.AcceptanceTestSteps.given;
import static nextstep.subway.acceptance.FavoriteSteps.로그인_상태에서_즐겨찾기에_추가한다;
import static nextstep.subway.acceptance.FavoriteSteps.로그인_안_한_상태로_즐겨찾기에_추가한다;
import static nextstep.subway.acceptance.FavoriteSteps.로그인을_한_채로_조회를_요청한다;
import static nextstep.subway.acceptance.StationSteps.지하철역_생성_요청;
import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("즐겨찾기 기능을 검증한다")
public class FavoriteAcceptanceTest extends AcceptanceTest {

	private static long 광교역;
	private static long 광교중앙역;

	@Override
	@BeforeEach
	public void setUp() {
		super.setUp();
		광교역 = 지하철역_생성_요청(관리자, "광교역").jsonPath().getLong("id");
		광교중앙역 = 지하철역_생성_요청(관리자, "광교중앙역").jsonPath().getLong("id");
	}

	/**
	 * Given 로그인한 상태로 즐겨찾기를 저장하고
	 * When 반환을 요청하면
	 * Then 반환된다.
	 */
	@DisplayName("로그인을 한 채로 즐겨찾기를 요청하면 성공한다.")
	@Test
	void saveAndGetFavorite() {
		//given
		로그인한_상태로_즐겨찾기가_추가됨();

		//then
		ExtractableResponse<Response> 응답 = 로그인을_한_채로_조회를_요청한다(관리자);

		//then
		assertThat(응답.statusCode()).isEqualTo(HttpStatus.OK.value());
	}

	/**
	 * When 로그인을 안 한 상태로 즐겨자기를 저장하면
	 * Then 실패한다.
	 */
	@DisplayName("로그인을 안 한 채로 즐겨찾기를 요청하면 실패한다.")
	@Test
	void saveFavoriteOnFail() {
		//when
		ExtractableResponse<Response> 응답 = 로그인_안_한_상태로_즐겨찾기에_추가한다(광교역, 광교중앙역);

		//then
		assertThat(응답.statusCode()).isEqualTo(HttpStatus.UNAUTHORIZED.value());
	}

	/**
	 * Given 즐겨찾기가 추가된다
	 * When 로그인을 한 채로 즐겨찾기를 삭제를 요청하면
	 * Then 성공한다
	 */
	@DisplayName("로그인을 한 채로 즐겨찾기를 삭제 요청을 하면 성공한다.")
	@Test
	void deleteFavorite() {
		//given
		ExtractableResponse<Response> response = 로그인한_상태로_즐겨찾기가_추가됨();
		String location = response.header("Location");

		//when
		ExtractableResponse<Response> 응답 = 즐겨찾기를_삭제한다(location);

		//then
		assertThat(응답.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
	}

	private ExtractableResponse<Response> 즐겨찾기를_삭제한다(String location) {
		return given(관리자)
				.contentType(MediaType.APPLICATION_JSON_VALUE)
				.when()
				.delete(location)
				.then()
				.log().all()
				.extract();
	}

	private ExtractableResponse<Response> 로그인한_상태로_즐겨찾기가_추가됨() {
		ExtractableResponse<Response> 응답 = 로그인_상태에서_즐겨찾기에_추가한다(관리자, 광교역, 광교중앙역);
		assertThat(응답.statusCode()).isEqualTo(HttpStatus.CREATED.value());
		return 응답;
	}
}
