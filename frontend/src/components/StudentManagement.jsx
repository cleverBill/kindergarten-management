import { useState, useEffect, useMemo } from 'react';
import { apiService } from '../services/api';
import CollapsePanel from './CollapsePanel';

const PAGE_SIZE = 6;

export default function StudentManagement({ activeTab }) {
  const [students, setStudents] = useState([]);
  const [formData, setFormData] = useState({
    name: '',
    grade: '',
    className: '',
    status: '正常'
  });
  const [showSuccess, setShowSuccess] = useState(false);
  const [showModal, setShowModal] = useState(false);
  const [currentPage, setCurrentPage] = useState(1);
  const [showConfirm, setShowConfirm] = useState(false);
  const [deleteTarget, setDeleteTarget] = useState(null);
  
  // 筛选状态
  const [searchText, setSearchText] = useState('');
  const [filterGrade, setFilterGrade] = useState('');
  const [filterClass, setFilterClass] = useState('');
  const [filterStatus, setFilterStatus] = useState('');

  useEffect(() => {
    loadStudents();
  }, []);

  const loadStudents = async () => {
    try {
      const data = await apiService.getStudents();
      setStudents(data);
    } catch (e) {
      console.error('加载学生列表失败:', e);
    }
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    if (!formData.name || !formData.grade || !formData.className) {
      showErrorDialog('请填写完整信息！');
      return;
    }

    try {
      await apiService.addStudent({
        name: formData.name,
        grade: formData.grade,
        className: formData.className,
        status: formData.status
      });
      setFormData({ name: '', grade: '', className: '', status: '正常' });
      setShowModal(false);
      setShowSuccess(true);
      setTimeout(() => setShowSuccess(false), 3000);
      loadStudents();
    } catch (e) {
      showErrorDialog(e.message);
    }
  };

  const confirmDelete = (id) => {
    setDeleteTarget(id);
    setShowConfirm(true);
  };

  const handleDelete = async () => {
    if (!deleteTarget) return;
    try {
      await apiService.deleteStudent(deleteTarget);
      setShowConfirm(false);
      setDeleteTarget(null);
      loadStudents();
    } catch (e) {
      showErrorDialog(e.message);
    }
  };

  const showErrorDialog = (message) => {
    const overlay = document.createElement('div');
    overlay.className = 'custom-dialog-overlay';
    overlay.innerHTML = `
      <div class="custom-dialog error-dialog">
        <div class="dialog-icon">❌</div>
        <div class="dialog-message">${message}</div>
        <button class="dialog-confirm-btn">确定</button>
      </div>
    `;
    document.body.appendChild(overlay);
    overlay.querySelector('.dialog-confirm-btn').onclick = () => overlay.remove();
    overlay.onclick = (e) => { if (e.target === overlay) overlay.remove(); };
  };

  const getStatusClass = (status) => {
    switch (status) {
      case '正常': return 'status-normal';
      case '需关注': return 'status-warning';
      case '优秀': return 'status-excellent';
      default: return 'status-normal';
    }
  };

  // 筛选逻辑
  const filteredStudents = useMemo(() => {
    return students.filter(student => {
      const matchSearch = !searchText || student.name.includes(searchText);
      const matchGrade = !filterGrade || student.grade === filterGrade;
      const matchClass = !filterClass || student.className.includes(filterClass);
      const matchStatus = !filterStatus || student.status === filterStatus;
      return matchSearch && matchGrade && matchClass && matchStatus;
    });
  }, [students, searchText, filterGrade, filterClass, filterStatus]);

  // 分页逻辑
  const totalPages = Math.ceil(filteredStudents.length / PAGE_SIZE);
  const startIndex = (currentPage - 1) * PAGE_SIZE;
  const endIndex = startIndex + PAGE_SIZE;
  const currentStudents = filteredStudents.slice(startIndex, endIndex);

  // 重置筛选
  const resetFilters = () => {
    setSearchText('');
    setFilterGrade('');
    setFilterClass('');
    setFilterStatus('');
    setCurrentPage(1);
  };

  // 当切换到添加学生时自动打开弹窗
  useEffect(() => {
    if (activeTab === 'addStudent') {
      setShowModal(true);
    }
  }, [activeTab]);

  const grades = ['小班', '中班', '大班', '一年级', '二年级'];
  const statuses = ['正常', '优秀', '需关注'];

  return (
    <div className="student-management">
      {/* 顶部操作栏 */}
      <div className="list-header">
        <h2>📋 学生列表</h2>
        <button className="add-btn" onClick={() => setShowModal(true)}>
          ➕ 添加学生
        </button>
      </div>

      {/* 成功提示 */}
      {showSuccess && <div className="success-message">✅ 添加成功！</div>}

      {/* 可折叠筛选栏 */}
      <CollapsePanel title={`🔍 筛选条件 (共 ${filteredStudents.length} 名学生)`} defaultOpen={false}>
        <div className="filter-content">
          <div className="filter-row">
            <div className="filter-group">
              <input
                type="text"
                className="filter-input"
                placeholder="搜索学生姓名..."
                value={searchText}
                onChange={(e) => { setSearchText(e.target.value); setCurrentPage(1); }}
              />
            </div>
            <div className="filter-group">
              <select
                className="filter-select"
                value={filterGrade}
                onChange={(e) => { setFilterGrade(e.target.value); setCurrentPage(1); }}
              >
                <option value="">全部年级</option>
                {grades.map(g => <option key={g} value={g}>{g}</option>)}
              </select>
            </div>
            <div className="filter-group">
              <select
                className="filter-select"
                value={filterStatus}
                onChange={(e) => { setFilterStatus(e.target.value); setCurrentPage(1); }}
              >
                <option value="">全部状态</option>
                {statuses.map(s => <option key={s} value={s}>{s}</option>)}
              </select>
            </div>
            <button className="filter-reset-btn" onClick={resetFilters}>重置</button>
          </div>
        </div>
      </CollapsePanel>

      {/* 学生列表 */}
      <div className="student-grid">
        {filteredStudents.length === 0 ? (
          <p className="empty-message">没有找到匹配的学生哦~ 🥰</p>
        ) : (
          currentStudents.map(student => (
            <div key={student.id} className="student-card">
              <h3>👧 {student.name}</h3>
              <p><strong>📚 年级:</strong> {student.grade}</p>
              <p><strong>🏠 班级:</strong> {student.className}</p>
              <p><strong>💖 状态:</strong> <span className={`status-tag ${getStatusClass(student.status)}`}>{student.status}</span></p>
              <div className="card-actions">
                <button className="card-btn card-btn-danger" onClick={() => confirmDelete(student.id)}>删除</button>
              </div>
            </div>
          ))
        )}
      </div>

      {/* 分页控件 */}
      {totalPages > 1 && (
        <div className="pagination">
          <button 
            className="page-btn" 
            disabled={currentPage === 1}
            onClick={() => setCurrentPage(1)}
          >首页</button>
          <button 
            className="page-btn" 
            disabled={currentPage === 1}
            onClick={() => setCurrentPage(p => p - 1)}
          >上一页</button>
          <span className="page-info">第 {currentPage} / {totalPages} 页</span>
          <button 
            className="page-btn" 
            disabled={currentPage === totalPages}
            onClick={() => setCurrentPage(p => p + 1)}
          >下一页</button>
          <button 
            className="page-btn" 
            disabled={currentPage === totalPages}
            onClick={() => setCurrentPage(totalPages)}
          >末页</button>
        </div>
      )}

      {/* 添加学生弹窗 */}
      {showModal && (
        <div className="modal-overlay" onClick={() => setShowModal(false)}>
          <div className="modal-content" onClick={e => e.stopPropagation()}>
            <div className="modal-header">
              <h2>添加新学生 🎉</h2>
              <button className="modal-close" onClick={() => setShowModal(false)}>×</button>
            </div>
            <form onSubmit={handleSubmit}>
              <div className="form-row">
                <div className="form-group">
                  <label>👧 姓名</label>
                  <input
                    type="text"
                    className="form-input"
                    value={formData.name}
                    onChange={(e) => setFormData({ ...formData, name: e.target.value })}
                    placeholder="请输入学生姓名"
                    required
                  />
                </div>
                <div className="form-group">
                  <label>📚 年级</label>
                  <select
                    className="form-select"
                    value={formData.grade}
                    onChange={(e) => setFormData({ ...formData, grade: e.target.value })}
                    required
                  >
                    <option value="">请选择年级</option>
                    <option value="小班">小班</option>
                    <option value="中班">中班</option>
                    <option value="大班">大班</option>
                    <option value="一年级">一年级</option>
                    <option value="二年级">二年级</option>
                  </select>
                </div>
              </div>
              <div className="form-row">
                <div className="form-group">
                  <label>🏠 班级</label>
                  <input
                    type="text"
                    className="form-input"
                    value={formData.className}
                    onChange={(e) => setFormData({ ...formData, className: e.target.value })}
                    placeholder="请输入班级名称"
                    required
                  />
                </div>
                <div className="form-group">
                  <label>💖 状态</label>
                  <select
                    className="form-select"
                    value={formData.status}
                    onChange={(e) => setFormData({ ...formData, status: e.target.value })}
                  >
                    <option value="正常">正常</option>
                    <option value="优秀">优秀</option>
                    <option value="需关注">需关注</option>
                  </select>
                </div>
              </div>
              <div className="form-actions">
                <button type="button" className="cancel-btn" onClick={() => setShowModal(false)}>取消</button>
                <button type="submit" className="submit-btn">添加学生</button>
              </div>
            </form>
          </div>
        </div>
      )}

      {/* 删除确认弹窗 */}
      {showConfirm && (
        <div className="modal-overlay">
          <div className="confirm-dialog">
            <div className="dialog-icon">⚠️</div>
            <div className="dialog-message">确定要删除这个学生吗？</div>
            <div className="dialog-actions">
              <button className="cancel-btn" onClick={() => setShowConfirm(false)}>取消</button>
              <button className="confirm-btn" onClick={handleDelete}>确定</button>
            </div>
          </div>
        </div>
      )}
    </div>
  );
}