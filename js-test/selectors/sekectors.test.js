import { itemTypesSelector } from '../../js/selectors';

it('item types selector', () => {
  // given
  const itemType1Id = '83b66aa7-3189-4e11-b4fa-fc73ded86ca5';
  const itemType2Id = '69d58d9c-f763-4481-a5a6-0defa1993329';
  const state = {
    entities: {
      itemTypes: {
        [itemType1Id]: {
          id: itemType1Id,
          name: 'itemType1',
          deleted: true,
        },
        [itemType2Id]: {
          id: itemType2Id,
          name: 'itemType2',
        },
      },
    },
  };

  // when
  const itemTypes = itemTypesSelector(state);

  // then
  expect(itemTypes).toHaveLength(1);
  expect(itemTypes[0]).toEqual({
    id: itemType2Id,
    name: 'itemType2',
  });
});
