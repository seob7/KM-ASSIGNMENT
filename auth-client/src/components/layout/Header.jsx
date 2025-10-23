import { Container, Nav, Navbar, Button } from "react-bootstrap";
import { useAuth } from "../../context/AuthContext";
import { useNavigate } from "react-router-dom";

function Header() {
  const { user, logout, isLoggedIn } = useAuth();
  const navigate = useNavigate();

  const handleLogout = async () => {
    try {
      await logout();
      navigate("/");
    } catch (error) {
      console.error("로그아웃 실패:", error.message);
    }
  };

  return (
    <Navbar expand="lg" className="bg-white shadow-sm">
      <Container>
        {/* 브랜드 영역  */}
        <Navbar.Brand
          onClick={() => navigate("/")}
          className="fw-bold text-primary"
          style={{ cursor: "pointer" }}
        >
          KM-ASSIGNMENT
        </Navbar.Brand>

        <Navbar.Toggle aria-controls="basic-navbar-nav" />
        <Navbar.Collapse id="basic-navbar-nav">
          <Nav className="me-auto">
            <Nav.Link
              onClick={() => navigate("/")}
              className="fw-medium"
              style={{ cursor: "pointer" }}
            >
              Home
            </Nav.Link>
          </Nav>

          <Nav className="ms-auto">
            {isLoggedIn() ? (
              <>
                <Nav.Link className="d-flex align-items-center me-3">
                  <span className="text-success fw-medium">
                    안녕하세요, {user || "사용자"}님!
                  </span>
                </Nav.Link>
                <Nav.Link>
                  <Button variant="danger" size="sm" onClick={handleLogout}>
                    로그아웃
                  </Button>
                </Nav.Link>
              </>
            ) : (
              <>
                <Nav.Link>
                  <Button
                    variant="primary"
                    size="sm"
                    onClick={() => navigate("/login")}
                  >
                    로그인
                  </Button>
                </Nav.Link>
                <Nav.Link>
                  <Button
                    variant="primary"
                    size="sm"
                    onClick={() => navigate("/signup")}
                  >
                    회원가입
                  </Button>
                </Nav.Link>
              </>
            )}
          </Nav>
        </Navbar.Collapse>
      </Container>
    </Navbar>
  );
}

export default Header;
