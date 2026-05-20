import { useState, useEffect } from 'react';
import { apiService } from '../services/api';

export default function RewardManagement() {
  const [rewards, setRewards] = useState([]);
  const [students, setStudents] = useState([]);
  const [formData, setFormData] = useState({
    studentId: '',
    type: '朵小花',
    reason: ''
  });

  useEffect(() => {
    loadData();
  }, []);

  const loadData = async () => {
    try {
      const [rewardsData, studentsData] = await Promise.all([
        apiService.getRewards(),
        apiService.getStudents()
      ]);
      setRewards(rewardsData);
      setStudents(studentsData);
      if (studentsData.length > 0) {
        setFormData(prev => ({ ...prev, studentId: studentsData[0].id.toString() }));
      }
    } catch (e) {
      console.error('加载数据失败:', e);
    }
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    if (!formData.reason) {
      alert('请填写奖励原因！');
      return;
    }

    try {
      await apiService.addReward({
        studentId: parseInt(formData.studentId),
        type: formData.type,
        reason: formData.reason
      });
      setFormData({ ...formData, reason: '' });
      loadData();
      const student = students.find(s => s.id === parseInt(formData.studentId));
      alert(`🎉 ${student?.name} 获得了${formData.type}！\n${formData.reason}`);
    } catch (e) {
      alert(e.message);
    }
  };

  const getStudentName = (studentId) => {
    const student = students.find(s => s.id === studentId);
    return student?.name || '未知';
  };

  return (
    <div className="reward-management">
      <div className="form-container">
        <h2>奖励学生 🏆</h2>
        <form onSubmit={handleSubmit}>
          <div className="form-group">
            <label>👧 选择学生</label>
            <select
              className="form-select"
              value={formData.studentId}
              onChange={(e) => setFormData({ ...formData, studentId: e.target.value })}
            >
              {students.map(s => (
                <option key={s.id} value={s.id}>👧 {s.name}</option>
              ))}
            </select>
          </div>
          <div className="form-group">
            <label>🏆 奖励类型</label>
            <select
              className="form-select"
              value={formData.type}
              onChange={(e) => setFormData({ ...formData, type: e.target.value })}
            >
              <option value="朵小花">🌸 小花</option>
              <option value="颗星星">⭐ 星星</option>
              <option value="个小红旗">🚩 小红旗</option>
              <option value="块小饼干">🍪 小饼干</option>
            </select>
          </div>
          <div className="form-group">
            <label>💝 奖励原因</label>
            <input
              type="text"
              className="form-input"
              value={formData.reason}
              onChange={(e) => setFormData({ ...formData, reason: e.target.value })}
              placeholder="为什么奖励这位学生呢？"
            />
          </div>
          <button type="submit" className="form-btn">颁发奖励</button>
        </form>
      </div>

      <div className="list-container">
        <h2>奖励记录 🎉</h2>
        {rewards.length === 0 ? (
          <p className="empty-message">还没有奖励记录~ 快去奖励学生吧！🎉</p>
        ) : (
          <div className="reward-list">
            {rewards.slice(-10).reverse().map(reward => (
              <div key={reward.id} className="list-item">
                🎉 <strong>{getStudentName(reward.studentId)}</strong> 获得了{reward.type}！
                {reward.reason && <span> - {reward.reason}</span>}
                <span className="list-date">({reward.date})</span>
              </div>
            ))}
          </div>
        )}
      </div>
    </div>
  );
}