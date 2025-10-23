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
import { validateRegisterForm } from "../utils/validation";

const SignUp = () => {
  const [formData, setFormData] = useState({
    email: "",
    password: "",
    confirmPassword: "",
    nickname: "",
  });
  const [isLoading, setIsLoading] = useState(false);
  const [validationError, setValidationError] = useState("");

  const { register, error, clearError } = useAuth();
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

    const validation = validateRegisterForm(formData);
    if (!validation.isValid) {
      setValidationError(validation.message);
      return;
    }

    setIsLoading(true);

    try {
      await register({
        email: formData.email,
        password: formData.password,
        nickname: formData.nickname,
      });
      navigate("/login");
    } catch (error) {
      console.error("회원가입 실패:", error.message);
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
              <h2 className="text-center mb-4 text-primary">회원가입</h2>

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
                  <Form.Label>닉네임</Form.Label>
                  <Form.Control
                    type="text"
                    name="nickname"
                    value={formData.nickname}
                    onChange={handleChange}
                    placeholder="닉네임을 입력하세요 (2-30자)"
                    required
                  />
                  <Form.Text className="text-muted">
                    2자 이상 30자 이하로 입력해주세요.
                  </Form.Text>
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

                <Form.Group className="mb-3">
                  <Form.Label>비밀번호 확인</Form.Label>
                  <Form.Control
                    type="password"
                    name="confirmPassword"
                    value={formData.confirmPassword}
                    onChange={handleChange}
                    placeholder="비밀번호를 다시 입력하세요"
                    required
                  />
                </Form.Group>

                <Button
                  type="submit"
                  variant="primary"
                  size="lg"
                  className="w-100 mb-3"
                  disabled={isLoading}
                >
                  {isLoading ? "가입 중..." : "회원가입"}
                </Button>
              </Form>

              <div className="text-center">
                <p className="mb-0">
                  이미 계정이 있으신가요?{" "}
                  <Link to="/login" className="text-decoration-none">
                    로그인
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

export default SignUp;
