export const storage = {
  setItem: (key, value) => {
    try {
      localStorage.setItem(key, typeof value === 'string' ? value : JSON.stringify(value));
    } catch (e) {
      console.error('Storage setItem error:', e);
    }
  },

  getItem: (key, defaultValue = null) => {
    try {
      const item = localStorage.getItem(key);
      if (item === null) return defaultValue;
      try {
        return JSON.parse(item);
      } catch {
        return item;
      }
    } catch (e) {
      console.error('Storage getItem error:', e);
      return defaultValue;
    }
  },

  removeItem: (key) => {
    try {
      localStorage.removeItem(key);
    } catch (e) {
      console.error('Storage removeItem error:', e);
    }
  },

  clear: () => {
    try {
      localStorage.clear();
    } catch (e) {
      console.error('Storage clear error:', e);
    }
  },

  getUser: () => {
    return {
      id: storage.getItem('userId'),
      username: storage.getItem('username'),
      role: storage.getItem('userRole'),
      token: storage.getItem('token'),
      kindergarten: storage.getItem('kindergarten')
    };
  },

  setUser: (user) => {
    storage.setItem('userId', user.id);
    storage.setItem('username', user.username);
    storage.setItem('userRole', user.role);
    storage.setItem('token', user.token);
    storage.setItem('kindergarten', user.kindergarten || '');
    storage.setItem('isLoggedIn', 'true');
  },

  clearUser: () => {
    storage.removeItem('userId');
    storage.removeItem('username');
    storage.removeItem('userRole');
    storage.removeItem('token');
    storage.setItem('isLoggedIn', 'false');
  },

  isLoggedIn: () => {
    return storage.getItem('isLoggedIn') === 'true';
  }
};