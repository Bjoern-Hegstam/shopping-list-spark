import { deleteItemType } from '../../js/actions/ItemTypeActions';
import * as types from '../../js/actions/types';

it('deleteItemType', () => {
// when
  const id = '933f2443-b11b-46ca-ba7e-25322fa649c1';

  // given
  const action = deleteItemType({ token: 'token', id });

  // then
  expect(action.type).toBe(types.DELETE_ITEM_TYPE);
  expect(action.meta.itemTypeId).toBe(id);
});
