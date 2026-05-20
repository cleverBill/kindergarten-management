import { useState } from 'react';
import { storage } from '../utils/storage';

const navSections = [
  {
    id: 'students',
    title: '👧 学生管理',
    items: [
      { id: 'studentList', label: '📋 学生列表' },
      { id: 'addStudent', label: '➕ 添加学生' },
      { id: 'attendance', label: '📅 考勤管理' },
      { id: 'homework', label: '📝 作业管理' },
      { id: 'rewards', label: '🏆 奖励记录' },
      { id: 'profiles', label: '📋 成长档案' }
    ]
  },
  {
    id: 'teachers',
    title: '👨‍🏫 教师管理',
    items: [
      { id: 'users', label: '👥 用户管理' },
      { id: 'performance', label: '📊 绩效统计' }
    ]
  },
  {
    id: 'operations',
    title: '📋 运营管理',
    items: [
      { id: 'fee', label: '💰 收费管理' },
      { id: 'pickup', label: '🚸 接送管理' },
      { id: 'announcements', label: '📢 公告管理' },
      { id: 'messages', label: '💬 消息中心' }
    ]
  },
  {
    id: 'system',
    title: '⚙️ 系统设置',
    items: [
      { id: 'dashboard', label: '📊 数据仪表盘' },
      { id: 'config', label: '🏠 园所配置' }
    ]
  }
];

export default function Navigation({ activeTab, onTabChange, onLogout }) {
  const [expandedSections, setExpandedSections] = useState(['students']);
  
  const user = storage.getUser();
  const kindergarten = user.kindergarten || '幼儿园管理系统';

  const toggleSection = (sectionId) => {
    setExpandedSections(prev =>
      prev.includes(sectionId)
        ? prev.filter(id => id !== sectionId)
        : [...prev, sectionId]
    );
  };

  return (
    <>
      <header className="app-header">
        <div className="app-header-inner">
          <div className="kindergarten-info">
            <span className="kindergarten-icon">🏠</span>
            <span className="kindergarten-name">{kindergarten}</span>
          </div>
          <div className="header-right">
            <span className="current-user">👤 {user.username}</span>
            <button className="logout-btn" onClick={onLogout}>退出登录</button>
          </div>
        </div>
      </header>

      <div className="main-nav">
        <div className="nav-grid">
          {navSections.map(section => (
            <div key={section.id} className="nav-box">
              <div 
                className={`nav-title ${expandedSections.includes(section.id) ? 'active' : ''}`}
                onClick={() => toggleSection(section.id)}
              >
                {section.title}
                <span className="nav-arrow">{expandedSections.includes(section.id) ? '▼' : '▶'}</span>
              </div>
              {expandedSections.includes(section.id) && (
                <div className="nav-items">
                  {section.items.map(item => (
                    <button
                      key={item.id}
                      className={`nav-item ${activeTab === item.id ? 'active' : ''}`}
                      onClick={() => onTabChange(item.id)}
                    >
                      {item.label}
                    </button>
                  ))}
                </div>
              )}
            </div>
          ))}
        </div>
      </div>
    </>
  );
}