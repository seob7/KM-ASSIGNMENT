import { Container, Row, Col, Card, Button } from "react-bootstrap";
import { useAuth } from "../context/AuthContext";
import { useNavigate } from "react-router-dom";

const Home = () => {
  const { user, isLoggedIn } = useAuth();
  const navigate = useNavigate();

  if (!isLoggedIn()) {
    return (
      <Container className="mt-5">
        <Row className="justify-content-center">
          <Col md={6}>
            <Card className="text-center shadow">
              <Card.Body className="p-5">
                <h2 className="mb-4">로그인이 필요한 페이지입니다.</h2>
                <p className="mb-4 text-muted">
                  서비스를 이용하시려면 로그인해주세요.
                </p>
                <Button
                  variant="primary"
                  size="lg"
                  onClick={() => navigate("/login")}
                  className="me-3"
                >
                  로그인
                </Button>
                <Button
                  variant="outline-primary"
                  size="lg"
                  onClick={() => navigate("/signup")}
                >
                  회원가입
                </Button>
              </Card.Body>
            </Card>
          </Col>
        </Row>
      </Container>
    );
  }

  return (
    <Container className="mt-5">
      <Row className="justify-content-center">
        <Col md={6}>
          <Card className="text-center shadow">
            <Card.Body className="p-5">
              <div className="mb-4">
                <h4 className="text-success">
                  안녕하세요, <strong>{user || "사용자"}</strong>님!
                </h4>
              </div>
              <p className="mb-4 text-muted">성공적으로 로그인하셨습니다.</p>
            </Card.Body>
          </Card>
        </Col>
      </Row>
    </Container>
  );
};

export default Home;
