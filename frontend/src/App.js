import { useState, useEffect } from 'react';
import { storage } from './utils/storage';
import Login from './components/Login';
import Navigation from './components/Navigation';
import StudentManagement from './components/StudentManagement';
import RewardManagement from './components/RewardManagement';
import AttendanceManagement from './components/AttendanceManagement';
import HomeworkManagement from './components/HomeworkManagement';
import UserManagement from './components/UserManagement';

export default function App() {
  const [isLoggedIn, setIsLoggedIn] = useState(false);
  const [activeTab, setActiveTab] = useState('studentList');

  useEffect(() => {
    setIsLoggedIn(storage.isLoggedIn());
  }, []);

  const handleLogin = () => {
    setIsLoggedIn(true);
  };

  const handleLogout = () => {
    storage.clearUser();
    setIsLoggedIn(false);
    setActiveTab('studentList');
  };

  const handleTabChange = (tabId) => {
    setActiveTab(tabId);
  };

  const renderContent = () => {
    switch (activeTab) {
      case 'studentList':
      case 'addStudent':
        return <StudentManagement />;
      case 'rewards':
        return <RewardManagement />;
      case 'attendance':
        return <AttendanceManagement />;
      case 'homework':
        return <HomeworkManagement />;
      case 'users':
        return <UserManagement />;
      case 'dashboard':
        return <Dashboard />;
      case 'fee':
        return <FeatureComingSoon name="收费管理" />;
      case 'pickup':
        return <FeatureComingSoon name="接送管理" />;
      case 'announcements':
        return <FeatureComingSoon name="公告管理" />;
      case 'messages':
        return <FeatureComingSoon name="消息中心" />;
      case 'config':
        return <FeatureComingSoon name="园所配置" />;
      case 'performance':
        return <FeatureComingSoon name="绩效统计" />;
      case 'profiles':
        return <FeatureComingSoon name="成长档案" />;
      default:
        return <StudentManagement />;
    }
  };

  function Dashboard() {
    return (
      <div className="dashboard">
        <h2>📊 数据仪表盘</h2>
        <div className="stats-overview">
          <div className="stat-card">
            <div className="stat-icon">👧</div>
            <div className="stat-value">--</div>
            <div className="stat-label">学生总数</div>
          </div>
          <div className="stat-card">
            <div className="stat-icon">✅</div>
            <div className="stat-value">--</div>
            <div className="stat-label">今日出勤</div>
          </div>
          <div className="stat-card">
            <div className="stat-icon">🏆</div>
            <div className="stat-value">--</div>
            <div className="stat-label">奖励总数</div>
          </div>
          <div className="stat-card">
            <div className="stat-icon">📝</div>
            <div className="stat-value">--</div>
            <div className="stat-label">作业记录</div>
          </div>
        </div>
        <div className="dashboard-tips">
          <h3>💡 使用提示</h3>
          <ul>
            <li>👧 点击"学生管理"查看和管理学生信息</li>
            <li>🏆 使用"奖励记录"激励表现优秀的学生</li>
            <li>📅 通过"考勤管理"记录学生出勤情况</li>
            <li>📝 使用"作业管理"跟踪学生作业完成情况</li>
          </ul>
        </div>
      </div>
    );
  }

  function FeatureComingSoon({ name }) {
    return (
      <div className="feature-coming-soon">
        <div className="coming-soon-icon">🚀</div>
        <h2>{name}</h2>
        <p>该功能正在开发中，敬请期待！</p>
      </div>
    );
  }

  if (!isLoggedIn) {
    return <Login onLogin={handleLogin} />;
  }

  return (
    <>
      <div className="nav-wrapper">
        <div className="decoration cloud">☁️</div>
        <div className="decoration star">⭐</div>
        <div className="decoration rainbow">🌈</div>
        <div className="decoration heart">❤️</div>

        <Navigation
          activeTab={activeTab}
          onTabChange={handleTabChange}
          onLogout={handleLogout}
        />
      </div>

      <main className="main-content">
        {renderContent()}
      </main>
    </>
  );
}