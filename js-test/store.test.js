import * as moment from 'moment';
import MockDate from 'mockdate';
import { logoutUserWhenTokenExpires } from '../js/store';
import * as types from '../js/actions/types';

describe('middleware', () => {
  describe('logoutUserWhenTokenExpires', () => {
    const token = 'eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJhIiwiZXhwIjoxNTM1ODMxNDY3LCJpYXQiOjE1MzU4Mjc4Njd9.SFRxV08lX0EJjadm1Fml_un6BAvtv_miM7z6SnA7iQA';
    let store;
    let next;

    beforeEach(() => {
      jest.useFakeTimers();
      MockDate.set(moment.unix(1532231467).toDate()); // 1 hour before the token expiration time

      store = {
        getState: () => ({ auth: { token } }),
        dispatch: jest.fn(),
      };
      next = jest.fn();
    });

    afterEach(() => {
      jest.runAllTimers();
    });

    it('sets timer based on token expiration time', () => {
      // when middleware is invoked
      logoutUserWhenTokenExpires(store)(next)({});

      // then a timer is set
      const oneHourInMilliseconds = 3600000000;
      expect(setTimeout).toHaveBeenCalledWith(expect.any(Function), oneHourInMilliseconds);

      // when the timer delay has passed
      jest.runAllTimers();

      // then
      expect(store.dispatch).toHaveBeenCalledWith({ type: types.LOGOUT });
    });

    it('sets a timer with non-negative delay when token is expired', () => {
      // given token expired 1ms ago
      MockDate.set(moment.unix(1535831468).toDate());

      // when
      logoutUserWhenTokenExpires(store)(next)({});

      // then
      expect(setTimeout).toHaveBeenCalledWith(expect.any(Function), 0);
    });

    it('does not set a new timer when one is already running', () => {
      // given
      logoutUserWhenTokenExpires(store)(next)({});
      expect(setTimeout).toHaveBeenCalledTimes(1);
      jest.resetAllMocks();

      // when
      logoutUserWhenTokenExpires(store)(next)({});

      // then
      expect(setTimeout).toHaveBeenCalledTimes(0);
    });

    it('clears timeout on logout', () => {
      // given
      logoutUserWhenTokenExpires(store)(next)({});

      // when
      logoutUserWhenTokenExpires(store)(next)({ type: types.LOGOUT });

      // then
      expect(clearTimeout).toHaveBeenCalledTimes(1);
    });

    it('starts a new timer on next invoke after logout', () => {
      // given
      logoutUserWhenTokenExpires(store)(next)({});
      logoutUserWhenTokenExpires(store)(next)({ type: types.LOGOUT });
      jest.resetAllMocks();

      // when
      logoutUserWhenTokenExpires(store)(next)({});

      // then
      expect(setTimeout).toHaveBeenCalledTimes(1);
    });

    it('starts a new timer on next invoke when previous timer has executed', () => {
      // given
      logoutUserWhenTokenExpires(store)(next)({});
      jest.runAllTimers();
      jest.resetAllMocks();

      // when
      logoutUserWhenTokenExpires(store)(next)({});

      // then
      expect(setTimeout).toHaveBeenCalledTimes(1);
    });
  });
});
