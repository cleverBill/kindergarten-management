import { useState } from 'react';
import { apiService } from '../services/api';
import { storage } from '../utils/storage';

export default function Login({ onLogin }) {
  const [username, setUsername] = useState('');
  const [password, setPassword] = useState('');
  const [kindergarten, setKindergarten] = useState('');
  const [error, setError] = useState('');
  const [isLoading, setIsLoading] = useState(false);
  const [isRegisterMode, setIsRegisterMode] = useState(false);
  const [successMessage, setSuccessMessage] = useState('');

  const handleLogin = async () => {
    if (!username || !password) {
      setError('请输入用户名和密码！');
      return;
    }

    setIsLoading(true);
    setError('');

    try {
      const response = await apiService.login(username, password);
      storage.setUser({
        id: response.userId,
        username: response.username,
        role: response.role,
        token: response.token,
        kindergarten: response.kindergarten
      });
      onLogin();
    } catch (e) {
      setError(e.message);
    } finally {
      setIsLoading(false);
    }
  };

  const handleRegister = async () => {
    if (!username || !password || !kindergarten) {
      setError('请填写完整信息！');
      return;
    }

    setIsLoading(true);
    setError('');

    try {
      await apiService.register(username, password, kindergarten);
      setSuccessMessage('注册成功！请等待管理员审核。');
      setUsername('');
      setPassword('');
      setKindergarten('');
      setTimeout(() => {
        setSuccessMessage('');
        setIsRegisterMode(false);
      }, 3000);
    } catch (e) {
      setError(e.message);
    } finally {
      setIsLoading(false);
    }
  };

  const handleKeyPress = (e) => {
    if (e.key === 'Enter') {
      isRegisterMode ? handleRegister() : handleLogin();
    }
  };

  return (
    <div className="login-container">
      <div className="login-box">
        <div className="login-icon">🏠</div>
        <h1>深圳市幼儿园管理系统</h1>
        
        {isRegisterMode ? (
          <>
            <input
              type="text"
              className="login-input"
              placeholder="用户名"
              value={username}
              onChange={(e) => setUsername(e.target.value)}
              onKeyPress={handleKeyPress}
            />
            <input
              type="password"
              className="login-input"
              placeholder="密码"
              value={password}
              onChange={(e) => setPassword(e.target.value)}
              onKeyPress={handleKeyPress}
            />
            <input
              type="text"
              className="login-input"
              placeholder="幼儿园名称"
              value={kindergarten}
              onChange={(e) => setKindergarten(e.target.value)}
              onKeyPress={handleKeyPress}
            />
            <button className="login-btn" onClick={handleRegister} disabled={isLoading}>
              {isLoading ? '注册中...' : '注册'}
            </button>
          </>
        ) : (
          <>
            <input
              type="text"
              className="login-input"
              placeholder="请输入用户名"
              value={username}
              onChange={(e) => setUsername(e.target.value)}
              onKeyPress={handleKeyPress}
            />
            <input
              type="password"
              className="login-input"
              placeholder="请输入密码"
              value={password}
              onChange={(e) => setPassword(e.target.value)}
              onKeyPress={handleKeyPress}
            />
            <button className="login-btn" onClick={handleLogin} disabled={isLoading}>
              {isLoading ? '登录中...' : '登录'}
            </button>
          </>
        )}

        {error && <div className="login-error">{error}</div>}
        {successMessage && <div className="success-message">{successMessage}</div>}

        <div className="login-switch">
          {isRegisterMode ? (
            <>
              已有账号？
              <button className="switch-btn" onClick={() => setIsRegisterMode(false)}>立即登录</button>
            </>
          ) : (
            <>
              还没有账号？
              <button className="switch-btn" onClick={() => setIsRegisterMode(true)}>立即注册</button>
            </>
          )}
        </div>
      </div>
    </div>
  );
}