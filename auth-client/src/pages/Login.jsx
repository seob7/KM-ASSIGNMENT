import { useState } from "react";
import {
  Container,
  Row,
  Col,
  Card,
  Form,
  Button,
  Alert,
} from "react-bootstrap";
import { useAuth } from "../context/AuthContext";
import { useNavigate, Link } from "react-router-dom";
import { validateLoginForm } from "../utils/validation";

const Login = () => {
  const [formData, setFormData] = useState({
    email: "",
    password: "",
  });
  const [isLoading, setIsLoading] = useState(false);
  const [validationError, setValidationError] = useState("");

  const { login, error, clearError } = useAuth();
  const navigate = useNavigate();

  const handleChange = (e) => {
    const { name, value } = e.target;
    setFormData((prev) => ({
      ...prev,
      [name]: value,
    }));
    // 에러가 있으면 입력 시 에러 클리어
    if (error) clearError();
    if (validationError) setValidationError("");
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    if (error) clearError();

    const validation = validateLoginForm(formData);
    if (!validation.isValid) {
      setValidationError(validation.message);
      return;
    }

    setIsLoading(true);

    try {
      await login(formData);
      navigate("/");
    } catch (error) {
      console.error("로그인 실패:", error.message);
    } finally {
      setIsLoading(false);
    }
  };

  return (
    <Container className="mt-5">
      <Row className="justify-content-center">
        <Col md={6} lg={4}>
          <Card className="shadow">
            <Card.Body className="p-4">
              <h2 className="text-center mb-4 text-primary">로그인</h2>

              {(error || validationError) && (
                <Alert variant="danger" className="mb-3">
                  {error || validationError}
                </Alert>
              )}

              <Form onSubmit={handleSubmit}>
                <Form.Group className="mb-3">
                  <Form.Label>이메일</Form.Label>
                  <Form.Control
                    type="email"
                    name="email"
                    value={formData.email}
                    onChange={handleChange}
                    placeholder="이메일을 입력하세요"
                    required
                  />
                </Form.Group>

                <Form.Group className="mb-3">
                  <Form.Label>비밀번호</Form.Label>
                  <Form.Control
                    type="password"
                    name="password"
                    value={formData.password}
                    onChange={handleChange}
                    placeholder="비밀번호를 입력하세요 (5-20자, 영문+숫자)"
                    required
                  />
                  <Form.Text className="text-muted">
                    5자 이상 20자 이하, 영문과 숫자를 포함해야 합니다.
                  </Form.Text>
                </Form.Group>

                <Button
                  type="submit"
                  variant="primary"
                  size="lg"
                  className="w-100 mb-3"
                  disabled={isLoading}
                >
                  {isLoading ? "로그인 중..." : "로그인"}
                </Button>
              </Form>

              <div className="text-center">
                <p className="mb-0">
                  계정이 없으신가요?
                  <Link to="/signup" className="text-decoration-none">
                    회원가입
                  </Link>
                </p>
              </div>
            </Card.Body>
          </Card>
        </Col>
      </Row>
    </Container>
  );
};

export default Login;
